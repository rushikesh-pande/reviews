package com.reviews.service;
import com.reviews.dto.*;
import com.reviews.entity.Review;
import com.reviews.exception.ReviewException;
import com.reviews.kafka.ReviewEventProducer;
import com.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class ReviewService {
    private final ReviewRepository repo;
    private final ReviewEventProducer producer;

    @Transactional
    public ReviewResponse submitReview(ReviewRequest req) {
        if (repo.existsByOrderIdAndCustomerIdAndProductId(req.getOrderId(),req.getCustomerId(),req.getProductId()))
            throw new ReviewException("Review already submitted for this order and product");
        Review r = Review.builder()
            .orderId(req.getOrderId()).customerId(req.getCustomerId())
            .productId(req.getProductId()).rating(req.getRating())
            .title(req.getTitle()).comment(req.getComment())
            .verifiedPurchase(true).approved(false).helpfulVotes(0).totalVotes(0)
            .status(Review.ReviewStatus.PENDING).mediaUrls(req.getMediaUrls())
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        r = repo.save(r);
        log.info("Review submitted id={} product={} rating={}", r.getId(), r.getProductId(), r.getRating());
        producer.publishSubmitted(r);
        return ReviewResponse.from(r);
    }

    @Transactional
    public ReviewResponse moderate(Long id, boolean approve) {
        Review r = repo.findById(id).orElseThrow(()->new ReviewException("Review not found: "+id));
        r.setApproved(approve);
        r.setStatus(approve ? Review.ReviewStatus.APPROVED : Review.ReviewStatus.REJECTED);
        r.setUpdatedAt(LocalDateTime.now());
        r = repo.save(r);
        if (approve) producer.publishApproved(r);
        return ReviewResponse.from(r);
    }

    @Transactional
    public ReviewResponse voteHelpful(Long id, boolean helpful) {
        Review r = repo.findById(id).orElseThrow(()->new ReviewException("Review not found: "+id));
        r.setTotalVotes(r.getTotalVotes()+1);
        if (helpful) { r.setHelpfulVotes(r.getHelpfulVotes()+1); producer.publishHelpful(r); }
        r.setUpdatedAt(LocalDateTime.now());
        return ReviewResponse.from(repo.save(r));
    }

    public List<ReviewResponse> getProductReviews(String productId) {
        return repo.findByProductIdAndApprovedTrue(productId).stream().map(ReviewResponse::from).collect(Collectors.toList());
    }

    public ProductRatingSummary getRatingSummary(String productId) {
        List<Review> reviews = repo.findByProductIdAndApprovedTrue(productId);
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        return ProductRatingSummary.builder().productId(productId)
            .averageRating(Math.round(avg*10.0)/10.0)
            .totalReviews(repo.countApprovedByProductId(productId))
            .fiveStar((int)reviews.stream().filter(r->r.getRating()==5).count())
            .fourStar((int)reviews.stream().filter(r->r.getRating()==4).count())
            .threeStar((int)reviews.stream().filter(r->r.getRating()==3).count())
            .twoStar((int)reviews.stream().filter(r->r.getRating()==2).count())
            .oneStar((int)reviews.stream().filter(r->r.getRating()==1).count()).build();
    }

    public List<ReviewResponse> getPendingReviews() {
        return repo.findByStatus(Review.ReviewStatus.PENDING).stream().map(ReviewResponse::from).collect(Collectors.toList());
    }
}
