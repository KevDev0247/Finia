package protect.Finia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import protect.Finia.models.LiabilitiesType;
import protect.Finia.datastructure.LiabilitiesTypeTreeLeaf;

@Dao
public interface LiabilitiesTypeDao {

    @Insert
    void insertLiabilitiesTypes(List<LiabilitiesType> liabilitiesTypes);

    @Query("SELECT \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesFirstLevelId, \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesFirstLevelName, \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesSecondLevelId, \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesSecondLevelName, \n" +
            "  liabilitiesThirdLevel.liabilitiesId AS liabilitiesThirdLevelId, \n" +
            "  liabilitiesThirdLevel.liabilitiesName AS liabilitiesThirdLevelName \n" +
            "FROM ( \n" +
            "  SELECT \n" +
            "    liabilitiesFirstLevel.liabilitiesId AS liabilitiesFirstLevelId, \n" +
            "    liabilitiesFirstLevel.liabilitiesName AS liabilitiesFirstLevelName, \n" +
            "    liabilitiesSecondLevel.liabilitiesId AS liabilitiesSecondLevelId, \n" +
            "    liabilitiesSecondLevel.liabilitiesName AS liabilitiesSecondLevelName \n" +
            "  FROM LiabilitiesType AS liabilitiesFirstLevel \n" +
            "  JOIN LiabilitiesType AS liabilitiesSecondLevel \n" +
            "  ON liabilitiesFirstLevel.liabilitiesParentType IS NULL \n" +
            "  AND liabilitiesFirstLevel.liabilitiesName = liabilitiesSecondLevel.liabilitiesParentType)" +
            "AS liabilitiesSecondLevelComposed LEFT JOIN LiabilitiesType AS liabilitiesThirdLevel \n" +
            "ON liabilitiesSecondLevelComposed.liabilitiesSecondLevelName = liabilitiesThirdLevel.liabilitiesParentType")
    List<LiabilitiesTypeTreeLeaf> queryLiabilitiesTypeTreeAsList();

    @Query("SELECT * FROM LiabilitiesType")
    List<LiabilitiesType> queryAllLiabilities();

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesName = :liabilitiesName")
    LiabilitiesType queryLiabilitiesByType (String liabilitiesName);
}
