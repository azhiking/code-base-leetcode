package com.azhito.leetcode;

//在 LeetCode 商店中， 有 n 件在售的物品。每件物品都有对应的价格。然而，也有一些大礼包，每个大礼包以优惠的价格捆绑销售一组物品。
//
// 给你一个整数数组 price 表示物品价格，其中 price[i] 是第 i 件物品的价格。另有一个整数数组 needs 表示购物清单，其中 needs[
//i] 是需要购买第 i 件物品的数量。
//
// 还有一个数组 special 表示大礼包，special[i] 的长度为 n + 1 ，其中 special[i][j] 表示第 i 个大礼包中内含第
//j 件物品的数量，且 special[i][n] （也就是数组中的最后一个整数）为第 i 个大礼包的价格。
//
// 返回 确切 满足购物清单所需花费的最低价格，你可以充分利用大礼包的优惠活动。你不能购买超出购物清单指定数量的物品，即使那样会降低整体价格。任意大礼包可无限
//次购买。
//
//
//
// 示例 1：
//
//
//输入：price = [2,5], special = [[3,0,5],[1,2,10]], needs = [3,2]
//输出：14
//解释：有 A 和 B 两种物品，价格分别为 ¥2 和 ¥5 。
//大礼包 1 ，你可以以 ¥5 的价格购买 3A 和 0B 。
//大礼包 2 ，你可以以 ¥10 的价格购买 1A 和 2B 。
//需要购买 3 个 A 和 2 个 B ， 所以付 ¥10 购买 1A 和 2B（大礼包 2），以及 ¥4 购买 2A 。
//
// 示例 2：
//
//
//输入：price = [2,3,4], special = [[1,1,0,4],[2,2,1,9]], needs = [1,2,1]
//输出：11
//解释：A ，B ，C 的价格分别为 ¥2 ，¥3 ，¥4 。
//可以用 ¥4 购买 1A 和 1B ，也可以用 ¥9 购买 2A ，2B 和 1C 。
//需要买 1A ，2B 和 1C ，所以付 ¥4 买 1A 和 1B（大礼包 1），以及 ¥3 购买 1B ， ¥4 购买 1C 。
//不可以购买超出待购清单的物品，尽管购买大礼包 2 更加便宜。
//
//
//
// 提示：
//
//
// n == price.length == needs.length
// 1 <= n <= 6
// 0 <= price[i], needs[i] <= 10
// 1 <= special.length <= 100
// special[i].length == n + 1
// 0 <= special[i][j] <= 50
// 生成的输入对于 0 <= j <= n - 1 至少有一个 special[i][j] 非零。
//
//
// Related Topics 位运算 记忆化搜索 数组 动态规划 回溯 状态压缩 👍 423 👎 0


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//leetcode submit region begin(Prohibit modification and deletion)
public class Solution638 {

    public static void main(String[] args) {

        //price = [2,5], special = [[3,0,5],[1,2,10]], needs = [3,2]

        List<Integer> price = new ArrayList<Integer>();
        price.add(2);
        price.add(5);

        List<List<Integer>> special = new ArrayList<List<Integer>>();
        List<Integer> specialItem1 = new ArrayList<Integer>();
        specialItem1.add(3);
        specialItem1.add(0);
        specialItem1.add(5);
        special.add(specialItem1);
        List<Integer> specialItem2 = new ArrayList<Integer>();
        specialItem2.add(1);
        specialItem2.add(2);
        specialItem2.add(10);
        special.add(specialItem2);

        List<Integer> needs = new ArrayList<Integer>();
        needs.add(3);
        needs.add(2);


        Solution638 solution638 = new Solution638();
        int total = solution638.shoppingOffers(price, special, needs);
        System.out.println("total = " + total);
    }

    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        // 过滤掉不符合条件的大礼包
        List<List<Integer>> filteredSpecial = filterSpecial(price, special, needs);
        // 递归购买大礼包，买不了就单独购买
        Map<List<Integer>, Integer> map = new HashMap<>();
        int totalSpecial = getTotalForShoppingOffers(price, filteredSpecial, needs, map);
        System.out.println("totalSpecial = " + totalSpecial);
        return totalSpecial;
    }

    public int getTotalNoSpecial(List<Integer> price, List<Integer> needs) {
        int total = 0;
        for (int i = 0; i < needs.size(); i++) {
            total += price.get(i) * needs.get(i);
        }
        return total;
    }

    public List<List<Integer>> filterSpecial(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        // 过滤掉 总价大于等于单独购买总价的大礼包
        List<List<Integer>> filteredSpecial = new ArrayList<List<Integer>>();
        for (List<Integer> specialItem : special) {
            int totalNoSpecial = 0;
            for (int j = 0; j < specialItem.size() - 1; j++) {
                totalNoSpecial += specialItem.get(j) * price.get(j);
            }
            if (totalNoSpecial > specialItem.get(specialItem.size() - 1)) {
                filteredSpecial.add(specialItem);
            }
        }

        // 过滤掉大礼包中 单个物品数量超过购买清单里的数量的大礼包
        for (List<Integer> specialItem : filteredSpecial) {
            if (!checkExceed(specialItem, needs)) {
                result.add(specialItem);
            }
        }
        return result;
    }

    public int getTotalForShoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs, Map<List<Integer>, Integer> map) {
        if (!map.containsKey(needs)) {
            int minTotal = getTotalNoSpecial(price, needs);
            for (List<Integer> specialItem : special) {
                int curTotal = 0;
                if (!checkExceed(specialItem, needs)) {
                    List<Integer> currentNeeds = new ArrayList<Integer>(needs);
                    for (int i = 0; i < currentNeeds.size(); i++) {
                        currentNeeds.set(i, currentNeeds.get(i) - specialItem.get(i));
                    }
                    curTotal += specialItem.get(specialItem.size() - 1) + getTotalForShoppingOffers(price, special, currentNeeds, map);
                    System.out.println("购买大礼包" + specialItem + "，价格是" + specialItem.get(specialItem.size() - 1) + "，当前总价是" + curTotal);
                    System.out.println("当前购买清单" + currentNeeds);
                    minTotal = Math.min(minTotal, curTotal);
                }
            }
            map.put(needs, minTotal);
        }
        return map.get(needs);
    }

    public boolean checkExceed(List<Integer> special, List<Integer> needs) {
        for (int j = 0; j < special.size() - 1; j++) {
            if (special.get(j) > needs.get(j)) {
                return true;
            }
        }
        return false;
    }

}
//leetcode submit region end(Prohibit modification and deletion)
