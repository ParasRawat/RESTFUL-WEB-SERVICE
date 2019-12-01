package WebService.Service;

import WebService.Shared.dto.AddressDTO;

import java.util.List;

public interface AddressesService {
    List<AddressDTO>  getAddresses(String id);

}
