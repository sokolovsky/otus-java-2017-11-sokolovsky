package ru.otus.l151.messageSystem;

/**
 * @author tully
 */
public interface Addressee {
    Address getAddress();

    void setMessageSystemContext(MessageSystemContext msContext);
}
