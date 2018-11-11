package kr.hkit.iotmobile.Dataform;

import java.util.List;

public class GetDustDataResult extends BaseResult {

    private List<Dust> dusts;

    public List<Dust> getDusts() {
        return dusts;
    }

    public void setDusts(List<Dust> dusts) {
        this.dusts = dusts;
    }
}
