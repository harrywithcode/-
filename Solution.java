//Before diving into the code, spend just 5 minutes to think about what data structure or algorithm you might use.
//Briefly describe your ideas in just a few words. You will start working on your code in the next problem.

//First, scan and store input data for further analyze.
//Second, build a map that key is manager's id, and value is a list of employees that under this manager. This gives us
//relationships in this company.
//Finally, update employee's rate if this employee's rate is bigger than his or her manager's, and return result.


//What's the space and time complexity of the final solution you used?
//space : O(N)
//Time: O(N)

//What would you do if you have as much time as you want to finish this project?
// 1. move Employee class to another java file, use Lombok Setter and Getter on Employee class to make this
// class encapsulation
//2. If the program catches exception, program should throw useful information to log.
//3. Add testing code.


//how to handle an addtional element blablabla 在每个node里面加个属性就好了
//In Employee class, add new attributes
//use ctrl + D to stop scanner input

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

//array[i][2]    array[i][0] means the grade of employee i.  array[i][1] means the manager of employee i.
//One manager may has more than one employees.


/*
 * The class is used to update rate value of each employee to make sure that every employee's rate is less or equal to
 * his or her manager's rate.
 * */
public class Solution {
    /*
    * The class represents employees that has three attributes, employee's id, employee's manager's id
    * and employee's rate
    * */
    public static class Employee {
        private int employeeId;
        private int managerId;
        private int rate;

        public Employee(final int employeeId, final int managerId, final int rate){
            this.employeeId = employeeId;
            this.managerId = managerId;
            this.rate = rate;
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        final List<Employee> inputData = solution.readInput();
        final Map<Integer, List<Employee>> relationTable = solution.storeRelationship(inputData);
        final int result = solution.calculateSumGrade(relationTable, inputData);

        System.out.println(result);
//        Others others = new Others();
//        others.helper();
    }

    /*
    * The method scans input data, build Employee objects and store the new Employee objects into a list.
    * */
    private List<Employee> readInput() {
        List<Employee> streamDataReceiver = new ArrayList<>();
        int employeeId = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextInt()) {
                int rate = scanner.nextInt();
                int managerId = scanner.nextInt();

                Employee employee = new Employee(employeeId, managerId, rate);
                employeeId++;
                streamDataReceiver.add(employee);
            }
        } catch (IllegalStateException e) {
            System.out.println("Scanner is closed and failed to scan input data");
        }

        return streamDataReceiver;
    }

    /*
    * The method read input data, find and store the manager-employee relationship among each employee into a map.
    * */
    private Map<Integer, List<Employee>> storeRelationship(final List<Employee> inputData) {
        /*Key is id of manager. Value is a list that contains one or more employees that under this manager.*/
        Map<Integer, List<Employee>> relationTable = new HashMap<>();

        for (Employee employee : inputData) {
            if (relationTable.containsKey(employee.managerId)) {
                relationTable.get(employee.managerId).add(employee);
            } else {
                relationTable.put(employee.managerId, new ArrayList<>(Arrays.asList(employee)));
            }
        }
        return relationTable;
    }

    /*
    * The method update employee's rate value if this employee's rate value is bigger than his or her manager's.
    * */
    private int calculateSumGrade(final Map<Integer, List<Employee>> relationTable, final List<Employee> inputData) {
        /*The CEO(boss) has no manager, so the key value is -1 and there is no co-worker of boss, so the length of list
        * is 1.
        */
        final Employee boss = relationTable.get(-1).get(0);
        int sum = boss.rate;

        /*Using Queue to do Breadth-first traversal*/
        Queue<Employee> queue = new LinkedList<>();
        queue.add(boss);

        while (!queue.isEmpty()) {
            Employee manager = queue.poll();
            int id = manager.employeeId;
            int rate = manager.rate;

            /*If there are some employees under this manager, check them one by one to see whether their rate is higher
            * than managers. If higher, than update to manager's rate.
            * */
            if (relationTable.containsKey(id)) {
                for (Employee employee : relationTable.get(id)) {
                    if (employee.rate > rate) {
                        employee.rate = rate;
                        inputData.get(employee.employeeId).rate = rate;
                    }
                    sum += employee.rate;
                    queue.offer(employee);
                }
            }
        }
        for (Employee employee : inputData) {
            System.out.println("value is " + employee.rate);
        }
        return sum;
    }
}