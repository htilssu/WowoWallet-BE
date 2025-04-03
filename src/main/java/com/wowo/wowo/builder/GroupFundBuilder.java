package com.wowo.wowo.builder;

import com.wowo.wowo.model.GroupFund;
import com.wowo.wowo.model.GroupFundWallet;
import com.wowo.wowo.model.User;

import java.time.LocalDate;

public class GroupFundBuilder {
    private String name;  // Không set default vì bắt buộc
    private String image = null;
    private String type = "general";
    private String description = null;
    private Long target;
    private LocalDate targetDate = LocalDate.now().plusDays(30);
    private User owner;  // Trường owner cần thiết để lưu trữ

    public GroupFundBuilder setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên quỹ không được để trống");
        }
        this.name = name;
        return this;
    }

    public GroupFundBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public GroupFundBuilder setType(String type) {
        this.type = (type != null && !type.trim().isEmpty()) ? type : "general";
        return this;
    }

    public GroupFundBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public GroupFundBuilder setTarget(Long target) {
        this.target = (target != null && target >= 0) ? target : 0L;
        return this;
    }

    public GroupFundBuilder setTargetDate(LocalDate targetDate) {
        this.targetDate = (targetDate != null && !targetDate.isBefore(LocalDate.now()))
                ? targetDate
                : LocalDate.now().plusDays(30);
        return this;
    }

    // Phương thức setOwner cần được định nghĩa ở đây
    public GroupFundBuilder setOwner(User owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner không được để trống");
        }
        this.owner = owner;
        return this;
    }

    public GroupFund build() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Tên quỹ là trường bắt buộc");
        }
        if (target == null) {
            throw new IllegalStateException("Số tiền mục tiêu là trường bắt buộc");
        }
        if (owner == null) {
            throw new IllegalStateException("Owner là trường bắt buộc");
        }

        GroupFund groupFund = new GroupFund();
        groupFund.setName(this.name);
        groupFund.setImage(this.image);
        groupFund.setType(this.type);
        groupFund.setDescription(this.description);
        groupFund.setTarget(this.target);
        groupFund.setTargetDate(this.targetDate);
        groupFund.setOwner(this.owner);

        GroupFundWallet wallet = new GroupFundWallet();
        wallet.setGroupFund(groupFund);
        groupFund.setWallet(wallet);

        return groupFund;
    }
}