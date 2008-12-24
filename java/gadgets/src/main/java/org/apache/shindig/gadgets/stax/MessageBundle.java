package org.apache.shindig.gadgets.stax;

import java.util.Collections;
import java.util.Map;

import org.apache.shindig.gadgets.stax.model.LocaleSpec.Direction;
import org.json.JSONObject;

public class MessageBundle {

  public static final MessageBundle EMPTY = new MessageBundle();

    private final Direction languageDirection;

    private final Map<String, String> messages;

    private final String jsonString;


    public MessageBundle() {
        this(Collections.<String, String>emptyMap());
    }

    public MessageBundle(final Map<String, String> messages) {
      this(messages, Direction.LTR);
  }
    public MessageBundle(final Map<String, String> messages, final Direction languageDirection) {
        this.languageDirection = languageDirection;
        this.messages = messages;
        this.jsonString = new JSONObject(messages).toString();
    }

    public Direction getLanguageDirection() {
        return languageDirection;
    }

  /**
   * @return A read-only view of the message bundle.
   */
  public Map<String, String> getMessages() {
      return messages;
  }

  /**
   * Return the contents as a JSON encoded string
   */
  public String toJSONString() {
      return jsonString;
  }
}

