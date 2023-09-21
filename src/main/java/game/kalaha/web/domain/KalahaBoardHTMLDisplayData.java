package game.kalaha.web.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class KalahaBoardHTMLDisplayData {
    List<Integer> rowSouth;
    List<Integer> rowNorth;
    Integer largePitSouth;
    Integer largePitNorth;
}
