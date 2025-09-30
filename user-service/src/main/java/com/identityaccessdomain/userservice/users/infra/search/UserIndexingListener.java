package com.identityaccessdomain.userservice.users.infra.search;

import com.identityaccessdomain.userservice.users.application.mapping.UserMapper;
import com.identityaccessdomain.userservice.users.domain.user.events.UserCreatedEvent;
import com.identityaccessdomain.userservice.users.domain.user.events.UserDeletedEvent;
import com.identityaccessdomain.userservice.users.domain.user.events.UserUpdatedEvent;
import com.identityaccessdomain.userservice.users.domain.user.repository.UserRepository;
import com.identityaccessdomain.userservice.users.infra.search.model.UserDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * user-service
 * <p>
 * Por que usar AFTER_COMMIT? porque só queremos indexar se a transação JPA foi confirmada;
 * evita inconsistências (index sem persistência).
 *
 * @author Juliane Maran
 * @since 28/09/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserIndexingListener {

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserSearchRepository searchRepository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserCreated(UserCreatedEvent event) {
    indexUser(event.userId());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserUpdated(UserUpdatedEvent event) {
    indexUser(event.userId());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserDeleted(UserDeletedEvent event) {
    searchRepository.deleteById(event.userId());
  }

  private void indexUser(Long id) {
    userRepository.findById(id).ifPresent(entity -> {
      UserDocument doc = userMapper.entityToDocument(entity);
      try {
        searchRepository.save(doc);
        log.info("Usuário {} indexado com sucesso no Elasticsearch", id);
      } catch (Exception e) {
        log.error("Falha ao indexar usuário {} no Elasticsearch", id, e);
      }
    });
  }

}
