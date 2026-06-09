package experiment;

/**
 * Lớp chạy tất cả thực nghiệm để kiểm tra.
 */
public class RunAllExperiments {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("CHẠY THỰC NGHIỆM HỆ THỐNG CALL CENTER WAITING LINE");
        System.out.println("==================================================");
        
        System.out.println("\n--- [Thực nghiệm 1] ---");
        new Exp1_PriorityQueue().run();
        
        System.out.println("\n--- [Thực nghiệm 2] ---");
        new Exp2_AgingAlgorithm().run();
        
        System.out.println("\n--- [Thực nghiệm 3] ---");
        new Exp3_HistoryLookup().run();
        
        System.out.println("\n==================================================");
        System.out.println("HOÀN THÀNH TẤT CẢ THỰC NGHIỆM THÀNH CÔNG!");
        System.out.println("==================================================");
    }
}
