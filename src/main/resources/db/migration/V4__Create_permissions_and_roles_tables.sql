-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create permissions table
CREATE TABLE IF NOT EXISTS permissions (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    resource VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(resource, action)
);

-- Create role_permissions junction table (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Create user_roles junction table (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Create user_permissions junction table for direct user permissions (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS user_permissions (
    user_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_roles_name ON roles(name);
CREATE INDEX idx_permissions_name ON permissions(name);
CREATE INDEX idx_permissions_resource ON permissions(resource);
CREATE INDEX idx_permissions_action ON permissions(action);
CREATE INDEX idx_role_permissions_role_id ON role_permissions(role_id);
CREATE INDEX idx_role_permissions_permission_id ON role_permissions(permission_id);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX idx_user_permissions_user_id ON user_permissions(user_id);
CREATE INDEX idx_user_permissions_permission_id ON user_permissions(permission_id);

-- Insert default roles
INSERT INTO roles (id, name, description) VALUES
    ('00000000-0000-0000-0000-000000000001', 'ROLE_USER', 'Regular user role'),
    ('00000000-0000-0000-0000-000000000002', 'ROLE_ADMIN', 'Administrator role'),
    ('00000000-0000-0000-0000-000000000003', 'ROLE_SUPER_ADMIN', 'Super Administrator role'),
    ('00000000-0000-0000-0000-000000000004', 'ROLE_SUPPORT', 'Support staff role'),
    ('00000000-0000-0000-0000-000000000005', 'ROLE_MERCHANT', 'Partner/Merchant role');

-- Insert default permissions
INSERT INTO permissions (id, name, description, resource, action) VALUES
    -- User permissions
    ('10000000-0000-0000-0000-000000000001', 'user:read', 'Read user information', 'user', 'read'),
    ('10000000-0000-0000-0000-000000000002', 'user:write', 'Create or update user', 'user', 'write'),
    ('10000000-0000-0000-0000-000000000003', 'user:delete', 'Delete user', 'user', 'delete'),
    -- Wallet permissions
    ('20000000-0000-0000-0000-000000000001', 'wallet:read', 'Read wallet information', 'wallet', 'read'),
    ('20000000-0000-0000-0000-000000000002', 'wallet:write', 'Create or update wallet', 'wallet', 'write'),
    ('20000000-0000-0000-0000-000000000003', 'wallet:delete', 'Delete wallet', 'wallet', 'delete'),
    ('20000000-0000-0000-0000-000000000004', 'wallet:transfer', 'Transfer money from wallet', 'wallet', 'transfer'),
    -- Transaction permissions
    ('30000000-0000-0000-0000-000000000001', 'transaction:read', 'Read transaction information', 'transaction', 'read'),
    ('30000000-0000-0000-0000-000000000002', 'transaction:write', 'Create transaction', 'transaction', 'write'),
    ('30000000-0000-0000-0000-000000000003', 'transaction:approve', 'Approve transaction', 'transaction', 'approve'),
    ('30000000-0000-0000-0000-000000000004', 'transaction:cancel', 'Cancel transaction', 'transaction', 'cancel'),
    -- Admin permissions
    ('40000000-0000-0000-0000-000000000001', 'admin:read', 'Read admin dashboard', 'admin', 'read'),
    ('40000000-0000-0000-0000-000000000002', 'admin:write', 'Admin write operations', 'admin', 'write'),
    ('40000000-0000-0000-0000-000000000003', 'admin:user_manage', 'Manage users', 'admin', 'user_manage'),
    ('40000000-0000-0000-0000-000000000004', 'admin:system_config', 'Configure system settings', 'admin', 'system_config'),
    -- Report permissions
    ('50000000-0000-0000-0000-000000000001', 'report:view', 'View reports', 'report', 'view'),
    ('50000000-0000-0000-0000-000000000002', 'report:export', 'Export reports', 'report', 'export');

-- Assign permissions to roles
-- USER role permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('00000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001'), -- user:read
    ('00000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001'), -- wallet:read
    ('00000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000004'), -- wallet:transfer
    ('00000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001'), -- transaction:read
    ('00000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000002'); -- transaction:write

-- ADMIN role permissions (all permissions except system_config)
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('00000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001'), -- user:read
    ('00000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002'), -- user:write
    ('00000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000003'), -- user:delete
    ('00000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000001'), -- wallet:read
    ('00000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000002'), -- wallet:write
    ('00000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000003'), -- wallet:delete
    ('00000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000004'), -- wallet:transfer
    ('00000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000001'), -- transaction:read
    ('00000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000002'), -- transaction:write
    ('00000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000003'), -- transaction:approve
    ('00000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000004'), -- transaction:cancel
    ('00000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000001'), -- admin:read
    ('00000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002'), -- admin:write
    ('00000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000003'), -- admin:user_manage
    ('00000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000001'), -- report:view
    ('00000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000002'); -- report:export

-- SUPER_ADMIN role permissions (all permissions)
INSERT INTO role_permissions (role_id, permission_id)
SELECT '00000000-0000-0000-0000-000000000003', id FROM permissions;

-- SUPPORT role permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('00000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000001'), -- user:read
    ('00000000-0000-0000-0000-000000000004', '20000000-0000-0000-0000-000000000001'), -- wallet:read
    ('00000000-0000-0000-0000-000000000004', '30000000-0000-0000-0000-000000000001'), -- transaction:read
    ('00000000-0000-0000-0000-000000000004', '50000000-0000-0000-0000-000000000001'); -- report:view

-- MERCHANT role permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('00000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000001'), -- user:read
    ('00000000-0000-0000-0000-000000000005', '20000000-0000-0000-0000-000000000001'), -- wallet:read
    ('00000000-0000-0000-0000-000000000005', '20000000-0000-0000-0000-000000000004'), -- wallet:transfer
    ('00000000-0000-0000-0000-000000000005', '30000000-0000-0000-0000-000000000001'), -- transaction:read
    ('00000000-0000-0000-0000-000000000005', '30000000-0000-0000-0000-000000000002'), -- transaction:write
    ('00000000-0000-0000-0000-000000000005', '50000000-0000-0000-0000-000000000001'); -- report:view
