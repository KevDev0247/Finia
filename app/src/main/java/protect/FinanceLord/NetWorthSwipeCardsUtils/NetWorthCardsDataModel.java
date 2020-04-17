package protect.FinanceLord.NetWorthSwipeCardsUtils;

public class NetWorthCardsDataModel {

    private int imageId;
    private String title;
    private String details;

    public NetWorthCardsDataModel(int imageId, String title, String details){
        this.imageId = imageId;
        this.title = title;
        this.details = details;
    }

    public int getImageId()     { return imageId; }
    public void setImageId(int imageId)     { this.imageId = imageId; }

    public String getTitle()    { return title; }
    public void setTitle(String title)  { this.title = title; }

    public String getDetails()      { return details; }
    public void setDetails(String details)      { this.details = details; }
}