package com.ecommerce.reviews.repository;

import com.ecommerce.reviews.entity.Review;
import com.ecommerce.reviews.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductIdAndStatus(Long productId, ReviewStatus status);

    List<Review> findByOrderId(Long orderId);

    List<Review> findByCustomerId(Long customerId);

    List<Review> findByStatus(ReviewStatus status);

    @Query("SELECT AVG(r.starRating) FROM Review r WHERE r.productId = :productId AND r.status = 'APPROVED'")
    Double findAverageRatingByProductId(Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.productId = :productId AND r.status = 'APPROVED'")
    Long countApprovedByProductId(Long productId);
}
