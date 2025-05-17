package es.daw2.microservice_user_v1.models.util;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public enum Role {
    ADMINISTRATOR(Arrays.asList(
        RolePermission.READ_ALL_RESERVATIONS,
        RolePermission.READ_ONE_RESERVATION,
        RolePermission.DISABLE_ONE_RESERVATION,
        RolePermission.CREATE_ONE_RESERVATION,
        RolePermission.UPDATE_ONE_RESERVATION,

        RolePermission.CREATE_ONE_WORKER,
        RolePermission.DELETE_ONE_WORKER,
        RolePermission.DISABLE_ONE_WORKER,
        RolePermission.UPDATE_ONE_WORKER
    )),

    WORKER(Arrays.asList(
        RolePermission.READ_ALL_RESERVATIONS,
        RolePermission.READ_ONE_RESERVATION,
        RolePermission.DISABLE_ONE_RESERVATION,
        RolePermission.CREATE_ONE_RESERVATION,
        RolePermission.UPDATE_ONE_RESERVATION
    )),

    NONE(Arrays.asList());


    @Getter
    @Setter
    private List<RolePermission> permissions;

    Role(List<RolePermission> permissions){
        this.permissions = permissions;
    }
}
