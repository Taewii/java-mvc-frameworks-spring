package productshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import productshop.domain.entities.Role;
import productshop.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.seedDatabase();
    }

    private void seedDatabase() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAndFlush(new Role("ROOT"));
            roleRepository.saveAndFlush(new Role("ADMIN"));
            roleRepository.saveAndFlush(new Role("MODERATOR"));
            roleRepository.saveAndFlush(new Role("USER"));
        }
    }
}
