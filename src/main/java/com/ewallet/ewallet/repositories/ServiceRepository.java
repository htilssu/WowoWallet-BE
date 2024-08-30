package com.ewallet.ewallet.repositories;

import com.ewallet.ewallet.link_service.Service;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, String> {

}
