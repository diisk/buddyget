package br.dev.diisk.application.seeds;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import br.dev.diisk.domain.entities.user.UserPerfil;
import br.dev.diisk.domain.enums.user.UserPermissionEnum;
import br.dev.diisk.domain.repositories.user.IUserPerfilRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PerfilSeed implements ApplicationRunner {

    private final IUserPerfilRepository userPerfilRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createPerfil("DEFAULT", 0, getDefaultPerfil());
    }

    private Boolean isEqualsPerfilPermissions(UserPerfil perfil, List<UserPermissionEnum> permissions) {
        if (perfil.getPermissions().size() != permissions.size())
            return false;
        for (UserPermissionEnum perm : permissions) {
            if (perfil.getPermissions().stream().noneMatch(perfilPerm -> perfilPerm == perm))
                return false;
        }

        return true;
    }

    private List<UserPermissionEnum> getDefaultPerfil() {
        List<UserPermissionEnum> permissions = new ArrayList<>();
        permissions.add(UserPermissionEnum.DEFAULT);
        return permissions;
    }

    private void createPerfil(String perfilName, Integer level, List<UserPermissionEnum> permissions) {
        UserPerfil clientePerfil = userPerfilRepository.findByName(perfilName);

        if (clientePerfil == null) {
            userPerfilRepository.save(new UserPerfil(perfilName, level, permissions));
            return;
        }
        
        if (clientePerfil.getLevel() != level || !isEqualsPerfilPermissions(clientePerfil, permissions)) {
            clientePerfil.setPermissions(permissions);
            clientePerfil.setLevel(level);
            userPerfilRepository.save(clientePerfil);
        }

    }

}
