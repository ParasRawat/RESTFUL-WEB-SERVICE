package WebService.RepositoryInterfaces;

import WebService.Entity.AddressEntity;
import WebService.Entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {

    List<AddressEntity> findAllByUserDetails(UserEntity entity);

}
