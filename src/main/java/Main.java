import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static final int execRound = 1000;  // 执行次数
    public static final int redPacketListSize = 3;  // 红包池大小
    public static final int needToRemoveSize = 1;  // 首次选中后，剩下的那些里，要去掉的空红包的数量
    public static final boolean needExchange = true;  // 是否交换

    public static void main(String[] args) {
        int getMoneyTimes = 0;
        int noGetMoneyTimes = 0;
        for (int i = 0; i < execRound; i++) {
            boolean getMoney = exec();
            if (getMoney) {
                getMoneyTimes++;
            } else {
                noGetMoneyTimes++;
            }
        }

        System.out.println("是否交换：" + needExchange);
        System.out.println("总次数：" + execRound);
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
        RedPacket redPacketRemain = getRemainOne(redPacketList);
        // 交换(or不交换)
        redPacketChosen = exchange(redPacketChosen, redPacketRemain);

        // 打开红包
        return showResult(redPacketChosen);
    }

    private static boolean showResult(RedPacket redPacketChosen) {
        return redPacketChosen.isHasMoney();
    }

    private static RedPacket exchange(RedPacket redPacketChosen, RedPacket redPacketRemain) {
        if (!needExchange) {
            return redPacketChosen;
        }

        redPacketChosen.setChosen(false);
        redPacketRemain.setChosen(true);
        return redPacketRemain;
    }

    private static RedPacket getRemainOne(List<RedPacket> redPacketList) {
        // 随机抽一个
        int index = (int) (Math.random() * (redPacketList.size()));
        return redPacketList.get(index);
    }

    private static void removeBlankInOthers(List<RedPacket> redPacketList) {
        int removeCount = 0;
        Iterator<RedPacket> iterator = redPacketList.iterator();
        while (iterator.hasNext()) {
            if (removeCount == needToRemoveSize) {
                break;
            }

            RedPacket redPacket = iterator.next();
            // 有钱的不能去掉
            if (redPacket.isHasMoney()) {
                continue;
            }
            iterator.remove();
            removeCount++;
        }
    }

    private static RedPacket firstChoose(List<RedPacket> redPacketList) {
        // 随机抽一个
        int index = (int) (Math.random() * (redPacketListSize));
        RedPacket redPacketChosen = redPacketList.get(index);
        redPacketChosen.setChosen(true);

        // 从池中去掉
        redPacketList.remove(redPacketChosen);

        return redPacketChosen;
    }

    private static List<RedPacket> buildRedPacketList() {
        // 初始化红包池
        List<RedPacket> redPacketList = new ArrayList<>();
        for (int i = 0; i < redPacketListSize; i++) {
            redPacketList.add(new RedPacket());
        }

        // 塞红包
        int index = (int) (Math.random() * (redPacketListSize));
        RedPacket redPacketHasMoney = redPacketList.get(index);
        redPacketHasMoney.setHasMoney(true);

        return redPacketList;
    }

}
