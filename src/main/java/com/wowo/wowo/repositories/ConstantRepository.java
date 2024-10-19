package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConstantRepository extends JpaRepository<Constant, String>,
                                            JpaSpecificationExecutor<Constant> {

}