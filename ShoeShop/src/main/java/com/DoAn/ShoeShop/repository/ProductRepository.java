package com.DoAn.ShoeShop.repository;

import com.DoAn.ShoeShop.entity.Category;
import com.DoAn.ShoeShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Override
    @Query(value = "from Product p where p.isDeleted = false and p.id = :id")
    Optional<Product> findById(Long id);
    @Query(value = "from Product p where p.isDeleted = false order by p.createAt desc limit 8")
    List<Product> getNewProduct();

    @Query(value = "from Product p where p.isDeleted = false order by rand() limit 4")
    List<Product> getRandomProduct();
    @Query(value = "from Product p where p.isDeleted = false ")
    List<Product> findAllNotDeleted();
    @Query(value = "select count(1) from Product p where p.isDeleted = false ")
    int countProducts();
    @Query(value = "from Product p where p.isDeleted = false and p.category = :category " +
            "and p.id != :id order by (p.totalRate / p.ratedTimes) desc limit 4")
    List<Product> getTop4HighestRatingOnCategory(Category category, Long id);
    @Query(value = "from Product p where p.isDeleted = false " +
            "order by (p.totalRate / p.ratedTimes) desc limit 4")
    List<Product> getTop4HighestRating();
    @Query(value = "from Product p where p.isDeleted = false and p.name like concat('%', :name, '%')")
    List<Product> findSuggestProduct(String name);

}
