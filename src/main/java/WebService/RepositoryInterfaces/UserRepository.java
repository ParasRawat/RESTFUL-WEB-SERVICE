package WebService.RepositoryInterfaces;

import WebService.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

        UserEntity findByEmail(String email);
        UserEntity findByUserId(String id);
        UserEntity findUserByEmailVerificationToken(String token);

        //NATIVE QUERIES
        @Query(value = "select * from users where first_name ='Paras'",
                countQuery = "select count(*) from users where first_name ='Paras'",
                nativeQuery = true)
        Page<UserEntity> findMyUserByName(Pageable pageableRequest);
}
