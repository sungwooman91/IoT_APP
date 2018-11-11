package kr.hkit.iotmobile.Dataform;

public class GetRankingDataResult extends BaseResult {

    private String[][] ranks;

    public String[][] getRanks() {
        return ranks;
    }

    public void setRanks(String[][] ranks) {
        this.ranks = ranks;
    }
}
