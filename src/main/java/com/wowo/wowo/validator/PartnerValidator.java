package com.wowo.wowo.validator;

import com.wowo.wowo.models.Partner;

public class PartnerValidator {

    public static boolean validatePartner(Partner partner) {

        if (partner.getDescription() == null || partner.getDescription().isEmpty()) {
            return false;
        }

        return true;
    }
}
