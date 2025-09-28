package com.identityaccessdomain.userservice.infra.search;

import com.identityaccessdomain.userservice.domain.user.events.UserCreatedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserDeletedEvent;
import com.identityaccessdomain.userservice.domain.user.events.UserUpdatedEvent;
import com.identityaccessdomain.userservice.domain.user.repository.UserRepository;
import com.identityaccessdomain.userservice.infra.search.model.UserDocument;
import com.identityaccessdomain.userservice.application.mapping.UserMapper;
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
      } catch (Exception e) {
        // retry/backoff or persist to a dead-letter store for later reprocessing
        // log and swallow or rethrow depending on policy
        log.error("Falha ao indexar usuário {} no ES", id, e);
      }
    });
  }


}
