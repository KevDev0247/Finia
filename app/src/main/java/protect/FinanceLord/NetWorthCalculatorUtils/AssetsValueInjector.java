package protect.FinanceLord.NetWorthCalculatorUtils;

import android.content.Context;

import java.util.List;

import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;

public class AssetsValueInjector {

    Context context;
    Long date;
    AssetsValueDao assetsValueDao;

    public AssetsValueInjector(Context context, Long date, AssetsValueDao assetsValueDao){
        this.context = context;
        this.date = date;
        this.assetsValueDao = assetsValueDao;
    }

    public void insertParentAssetsValue(List<Float> parentAssets){

        AssetsValue totalAssets = new AssetsValue();
        totalAssets.setAssetsValue(parentAssets.get(0));
        totalAssets.setAssetsId(35);
        totalAssets.setDate(date);

        if (totalAssets.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(totalAssets.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(totalAssets);
            }
        } else {
            assetsValueDao.insertAssetValue(totalAssets);
        }


        AssetsValue liquidAssets = new AssetsValue();
        liquidAssets.setAssetsValue(parentAssets.get(1));
        liquidAssets.setAssetsId(32);
        liquidAssets.setDate(date);

        if (liquidAssets.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(liquidAssets.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(liquidAssets);
            }
        } else {
            assetsValueDao.insertAssetValue(liquidAssets);
        }


        AssetsValue investedAssets = new AssetsValue();
        investedAssets.setAssetsValue(parentAssets.get(2));
        investedAssets.setAssetsId(33);
        investedAssets.setDate(date);

        if (investedAssets.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(investedAssets.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(investedAssets);
            }
        } else {
            assetsValueDao.insertAssetValue(investedAssets);
        }


        AssetsValue personalAssets = new AssetsValue();
        personalAssets.setAssetsValue(parentAssets.get(3));
        personalAssets.setAssetsId(34);
        personalAssets.setDate(date);

        if (personalAssets.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(personalAssets.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(personalAssets);
            }
        } else {
            assetsValueDao.insertAssetValue(personalAssets);
        }


        AssetsValue taxableAccounts = new AssetsValue();
        taxableAccounts.setAssetsValue(parentAssets.get(4));
        taxableAccounts.setAssetsId(29);
        taxableAccounts.setDate(date);

        if (taxableAccounts.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(taxableAccounts.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(taxableAccounts);
            }
        } else {
            assetsValueDao.insertAssetValue(taxableAccounts);
        }


        AssetsValue retirementAccounts = new AssetsValue();
        retirementAccounts.setAssetsValue(parentAssets.get(5));
        retirementAccounts.setAssetsId(30);
        retirementAccounts.setDate(date);

        if (retirementAccounts.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(retirementAccounts.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(retirementAccounts);
            }
        } else {
            assetsValueDao.insertAssetValue(retirementAccounts);
        }


        AssetsValue ownershipInterest = new AssetsValue();
        ownershipInterest.setAssetsValue(parentAssets.get(6));
        ownershipInterest.setAssetsId(31);
        ownershipInterest.setDate(date);
        if (ownershipInterest.getAssetsPrimaryId() != 0){
            List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(ownershipInterest.getAssetsPrimaryId());
            if (!assetsValues.isEmpty()){
                assetsValueDao.updateAssetValue(ownershipInterest);
            }
        } else {
            assetsValueDao.insertAssetValue(ownershipInterest);
        }
    }
}
