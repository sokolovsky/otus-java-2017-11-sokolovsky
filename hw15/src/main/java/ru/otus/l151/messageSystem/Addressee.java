package ru.otus.l151.messageSystem;

import ru.otus.sokolovsky.hw15.domain.MessageSystemContext;

/**
 * @author tully
 */
public interface Addressee {
    Address getAddress();

    void setMessageSystemContext(MessageSystemContext msContext);
}
