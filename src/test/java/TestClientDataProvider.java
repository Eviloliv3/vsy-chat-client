import static java.util.List.of;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import de.vsy.shared_transmission.dto.authentication.AuthenticationDTO;
import java.util.List;

public class TestClientDataProvider {

  public static final AuthenticationDTO FRANK_AUTH_CORRECT = AuthenticationDTO.valueOf("frank1",
      "login");
  public static final AuthenticationDTO FRANK_AUTH_FALSE = AuthenticationDTO.valueOf("frank1",
      "login");
  public static final AuthenticationDTO MARKUS_AUTH_CORRECT = AuthenticationDTO.valueOf("markus1",
      "login");
  public static final CommunicatorDTO FRANK_COMM_CORRECT = CommunicatorDTO.valueOf(15001,
      "Frank Relation1");
  public static final CommunicatorDTO MARKUS_COMM_CORRECT = CommunicatorDTO.valueOf(15002,
      "Markus Relation2");
  public static final CommunicatorDTO ADRIAN_COMM_CORRECT = CommunicatorDTO.valueOf(15003,
      "Adrian Relation3");
  public static final CommunicatorDTO PETER_COMM_CORRECT = CommunicatorDTO.valueOf(15004,
      "Peter Relation4");
  public static final CommunicatorDTO MAX_COMM_CORRECT = CommunicatorDTO.valueOf(15005,
      "Max Status1");
  public static final CommunicatorDTO THOMAS_COMM_CORRECT = CommunicatorDTO.valueOf(15006,
      "Thomas Status2");
  public static final List<AuthenticationDTO> AUTH_CLIENT_LIST = of(FRANK_AUTH_CORRECT,
      MARKUS_AUTH_CORRECT);
  public static final List<CommunicatorDTO> RELA_CLIENT_LIST = of(MARKUS_COMM_CORRECT,
      ADRIAN_COMM_CORRECT, PETER_COMM_CORRECT, MAX_COMM_CORRECT, THOMAS_COMM_CORRECT);
}

