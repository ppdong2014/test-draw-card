import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int getMoneyTimes = 0;
        int noGetMoneyTimes = 0;
        for (int i = 0; i < Config.execRound; i++) {
            boolean getMoney = exec();
            if (getMoney) {
                getMoneyTimes++;
            } else {
                noGetMoneyTimes++;
            }
        }

        System.out.println("是否交换：" + Config.needExchange);
        System.out.println("总次数：" + Config.execRound);
        System.out.println("拿到钱次数：" + getMoneyTimes);
        System.out.println("没拿到钱次数：" + noGetMoneyTimes);
    }

    public static boolean exec() {
        // 构建n张红包（随机其中一个有钱）
        List<RedPacket> redPacketList = buildRedPacketList();

        // 随机抽取其中一个
        RedPacket redPacketChosen = firstChoose(redPacketList);

        // 去掉剩余之中空的
        removeBlankInOthers(redPacketList);

        // 拿到（去空之后）剩余之中的一个
        RedPacket redPacketRemain = getOneInRemain(redPacketList);
        // 交换(or不交换)
        redPacketChosen = exchange(redPacketChosen, redPacketRemain);

        // 打开红包
        return showResult(redPacketChosen);
    }

    private static boolean showResult(RedPacket redPacketChosen) {
        return redPacketChosen.isHasMoney();
    }

    private static RedPacket exchange(RedPacket redPacketChosen, RedPacket redPacketRemain) {
        if (!Config.needExchange) {
            return redPacketChosen;
        }

        redPacketChosen.setChosen(false);
        redPacketRemain.setChosen(true);
        return redPacketRemain;
    }

    private static RedPacket getOneInRemain(List<RedPacket> redPacketList) {
        // 随机抽一个
        int index = (int) (Math.random() * (redPacketList.size()));
        return redPacketList.get(index);
    }

    private static void removeBlankInOthers(List<RedPacket> redPacketList) {
        int removeCount = 0;
        Iterator<RedPacket> iterator = redPacketList.iterator();
        while (iterator.hasNext()) {
            if (removeCount == Config.needToRemoveSize) {
                break;
            }

            RedPacket redPacket = iterator.next();
            // 有钱的不能去掉(可配置有钱的也能去掉)
            if (redPacket.isHasMoney() && !Config.canRemoveRedPacketHasMoney) {
                continue;
            }
            iterator.remove();
            removeCount++;
        }
    }

    private static RedPacket firstChoose(List<RedPacket> redPacketList) {
        // 随机抽一个
        int index = (int) (Math.random() * (Config.redPacketListSize));
        RedPacket redPacketChosen = redPacketList.get(index);
        redPacketChosen.setChosen(true);

        // 从池中去掉
        redPacketList.remove(redPacketChosen);

        return redPacketChosen;
    }

    private static List<RedPacket> buildRedPacketList() {
        // 初始化红包池
        List<RedPacket> redPacketList = new ArrayList<>();
        for (int i = 0; i < Config.redPacketListSize; i++) {
            redPacketList.add(new RedPacket());
        }

        // 塞红包
        int index = (int) (Math.random() * (Config.redPacketListSize));
        RedPacket redPacketHasMoney = redPacketList.get(index);
        redPacketHasMoney.setHasMoney(true);

        return redPacketList;
    }

}
