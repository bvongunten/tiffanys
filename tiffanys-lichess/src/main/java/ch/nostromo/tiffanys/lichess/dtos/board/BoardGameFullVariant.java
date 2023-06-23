package ch.nostromo.tiffanys.lichess.dtos.board;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BoardGameFullVariant {
    String key;
    String name;
    @SerializedName(value = "short")
    String shortName;
}
