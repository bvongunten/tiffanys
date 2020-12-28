package ch.nostromo.tiffanys.lichess.dtos.commons;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Count {

    Long ai;
    Long all;
    Long bookmark;
    Long draw;
    Long drawH;

    @SerializedName(value = "import")
    Long imported;

    Long loss;
    Long lossH;
    Long me;
    Long playing;
    Long rated;
    Long win;
    Long winH;
}
