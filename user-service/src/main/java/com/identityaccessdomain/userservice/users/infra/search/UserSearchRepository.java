package com.identityaccessdomain.userservice.users.infra.search;

import com.identityaccessdomain.userservice.users.infra.search.model.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * user-service
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Repository
public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {

  // Você pode adicionar buscas customizadas, ex: findByFirstNameContaining(String name)

}