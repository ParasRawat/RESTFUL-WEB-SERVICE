package WebService.ServiceImplementation;

import WebService.Entity.AddressEntity;
import WebService.Entity.UserEntity;
import WebService.Exceptions.UserServiceException;
import WebService.RepositoryInterfaces.AddressRepository;
import WebService.RepositoryInterfaces.UserRepository;
import WebService.Service.AddressesService;
import WebService.Shared.dto.AddressDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImplementation implements AddressesService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;
    @Override
    public List<AddressDTO> getAddresses(String id) {
        List<AddressDTO> returnValue=new ArrayList<>();

        UserEntity userEntity=userRepository.findByUserId(id);

        if(userEntity==null) throw new UserServiceException("USER NOT FOUND");

        Iterable<AddressEntity> addressEntities=addressRepository.findAllByUserDetails(userEntity);

        for(AddressEntity addressEntity:addressEntities){
            returnValue.add(new ModelMapper().map(addressEntity,AddressDTO.class));

        }



        return returnValue;
    }

    @Override
    public AddressDTO getAddress(String addressId) {
        AddressEntity addressEntity=addressRepository.findByAddressId(addressId);

        AddressDTO addressDTO=new ModelMapper().map(addressEntity,AddressDTO.class);
        return addressDTO;
    }
}
