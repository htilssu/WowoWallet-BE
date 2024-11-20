package com.wowo.wowo.repository;

import com.wowo.wowo.model.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConstantRepository extends JpaRepository<Constant, String>,
                                            JpaSpecificationExecutor<Constant> {

}