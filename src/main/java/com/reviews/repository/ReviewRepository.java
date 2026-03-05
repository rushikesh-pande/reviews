package com.reviews.repository;
import com.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdAndApprovedTrue(String productId);
    List<Review> findByCustomerId(String customerId);
    List<Review> findByStatus(Review.ReviewStatus status);
    boolean existsByOrderIdAndCustomerIdAndProductId(String o, String c, String p);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId=:productId AND r.approved=true")
    Double findAverageRatingByProductId(String productId);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.productId=:productId AND r.approved=true")
    long countApprovedByProductId(String productId);
}
