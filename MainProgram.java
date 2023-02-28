import java.util.Scanner;
import java.io.*;

class Process {
    int burstTime, arrivalTime, turnAroundTime = 0, waitingTime = 0, copy_of_burstTime;
    String id;

    Process(int Atime, int Btime, String s) {
        arrivalTime = Atime;
        burstTime = Btime;
        copy_of_burstTime = Btime;
        id = s;
    }
}

class Roundrobin {
    int burstTime, pr, tat, waitingTime, copy_of_burstTime, arrivalTime;
}

public class MainProgram {
    static int sum = 0;

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        String s;
        int choice;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("Please enter the number of selected process respecitively: ");
            System.out.println("Enter 1 for NonPreemptive SJF");
            System.out.println("Enter 2 for Preemptive SJF");
            System.out.println("Enter 3 for Round Robin");
            System.out.println("Enter 0 to exit");
            System.out.print("Enter your choice here: ");
            choice = input.nextInt();
            String schedulling = null;
            if (choice == 1)
                schedulling = "NonPreemptive SJF";
            else if (choice == 2)
                schedulling = "Preemptive SJF";
            else if (choice == 3)
                schedulling = "Round Robin";
            else
                schedulling = "ERROR! Please choose a schedulling process!";

            System.out.println("----------------------------------------------------");
            if (choice == 0)
                System.out.println("Thanks for using our Process Schedulling Simulator");
            else
                System.out.println("Schedulling: " + schedulling);
            System.out.println("----------------------------------------------------");

            switch (choice) {
            case 1:
                mainNonPreemptiveSjf();
                break;
            case 2:
                mainPreemptiveSjf();
                break;
            case 3:
                mainRoundRobin();
                break;
            }
        } while (choice != 0);
    }

    // ------------------------------------------------------------------------------------------------------------------
    public static void mainNonPreemptiveSjf() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int numberOfProcess;
        do {
            System.out.println("enter the total number of processes in the range (3 to 10)");
            numberOfProcess = Integer.parseInt(br.readLine());
        } while (numberOfProcess < 3 || numberOfProcess > 10);

        Process p[] = new Process[numberOfProcess];
        for (int i = 0; i < numberOfProcess; i++) {
            System.out.println("enter the process Id, arrival time and Burst time for " + (i + 1) + "th process");
            String s = br.readLine();
            int a = Integer.parseInt(br.readLine());
            int b = Integer.parseInt(br.readLine());
            sum += b;
            p[i] = new Process(a, b, s);
        }
        sjf(p);
    }
    // ------------------------------------------------------------------------------------------------------------------

    public static void mainPreemptiveSjf() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n;
        do {
            System.out.println("Please enter the number of Processes range (3 to 10): ");
            n = Integer.parseInt(br.readLine());
        } while (n < 3 || n > 10);
        int proc[][] = new int[n + 1][4];// proc[][0] is the AT array,[][1] - RT,[][2] - WT,[][3] - TT
        for (int i = 1; i <= n; i++) {
            System.out.println("Please enter the Arrival Time for Process " + i + ": ");
            proc[i][0] = Integer.parseInt(br.readLine());
            System.out.println("Please enter the Burst Time for Process " + i + ": ");
            proc[i][1] = Integer.parseInt(br.readLine());
        }
        System.out.println();

        // Calculation of Total Time and Initialization of Time Chart array
        int total_time = 0;
        for (int i = 1; i <= n; i++) {
            total_time += proc[i][1];
        }
        int time_chart[] = new int[total_time];

        System.out.println("-------------------Gantt Chart----------------------");
        for (int i = 0; i < total_time; i++) {
            // Selection of shortest process which has arrived
            int sel_proc = 0;
            int min = 99999;
            for (int j = 1; j <= n; j++) {
                if (proc[j][0] <= i)// Condition to check if Process has arrived
                {
                    if (proc[j][1] < min && proc[j][1] != 0) {
                        min = proc[j][1];
                        sel_proc = j;
                    }
                }
            }

            // Assign selected process to current time in the Chart
            time_chart[i] = sel_proc;

            // Decrement Remaining Time of selected process by 1 since it has been assigned
            // the CPU for 1 unit of time
            proc[sel_proc][1]--;

            // WT and TT Calculation
            for (int j = 1; j <= n; j++) {
                if (proc[j][0] <= i) {
                    if (proc[j][1] != 0) {
                        proc[j][3]++;// If process has arrived and it has not already completed execution its TT is
                                     // incremented by 1
                        if (j != sel_proc)// If the process has not been currently assigned the CPU and has arrived its
                                          // WT is incremented by 1
                            proc[j][2]++;
                    } else if (j == sel_proc)// This is a special case in which the process has been assigned CPU and
                                             // has completed its execution
                        proc[j][3]++;
                }
            }

            // Printing the Time Chart
            if (i != 0) {
                if (sel_proc != time_chart[i - 1])
                // If the CPU has been assigned to a different Process we need to print the
                // current value of time and the name of
                // the new Process
                {
                    System.out.print("--" + i + "--P" + sel_proc);
                }
            } else// If the current time is 0 i.e the printing has just started we need to print
                  // the name of the First selected Process
                System.out.print(i + "--P" + sel_proc);
            if (i == total_time - 1)// All the process names have been printed now we have to print the time at
                                    // which execution ends
                System.out.print("--" + (i + 1));

        }
        System.out.println();
        System.out.println();

        // Printing the WT and TT for each Process
        System.out.println("--------------------Table----------------------------");
        System.out.println("P\t TT \t WT");
        for (int i = 1; i <= n; i++) {
            System.out.printf("%d\t%2d\t%2d", i, proc[i][3], proc[i][2]);
            System.out.println();
        }

        System.out.println();

        // Printing the average WT & TT
        float WT = 0, TT = 0;
        for (int i = 1; i <= n; i++) {
            WT += proc[i][2];
            TT += proc[i][3];
        }
        System.out.println("Total Waiting time: " + WT);
        WT /= n;
        System.out.println("The Average Waiting time is: " + WT);
        System.out.println("Total turnaround time: " + TT);
        TT /= n;
        System.out.println("The Average Turnaround time is: " + TT);
    }

    public static void mainRoundRobin() {
        int burstTime, pr, tat, waitingTime, copy_of_burstTime, arrivalTime;

        Scanner sc = new Scanner(System.in);
        int n;
        do {
            System.out.println("enter no. of processes range (3 to 10) : ");
            n = sc.nextInt();
        } while (n < 3 || n > 10);
        System.out.println("enter time quantum : ");
        int timeQuantam = sc.nextInt();
        int sum = 0;
        Roundrobin process[] = new Roundrobin[n + 1]; // creatng array of objects
        for (int i = 0; i <= n; i++)
            process[i] = new Roundrobin();
        // INPUT
        for (int m = 1; m <= n; m++) {
            process[m].pr = m;
            System.out.println("enter burst time of process " + m);
            process[m].burstTime = sc.nextInt();
            sum += process[m].burstTime;
            process[m].copy_of_burstTime = process[m].burstTime;
        }

        for (int m = 1; m <= n; m++) {

            System.out.println("enter arrival time of process " + m);
            process[m].arrivalTime = sc.nextInt();
        }

        for (int i = 1; i <= n; i++) // sort according to arrival
        {
            for (int j = 1; j < n; j++) {
                if (process[j].arrivalTime > process[j + 1].arrivalTime) {
                    Roundrobin temp = process[j];
                    process[j] = process[j + 1];
                    process[j + 1] = temp;
                }
            }
        }

        int t = 0;
        while (t < sum) {
            for (int j = 1; j <= n; j++) {
                if (process[j].burstTime > 0 && t >= process[j].arrivalTime) {
                    if (process[j].burstTime > timeQuantam) {
                        process[j].burstTime -= timeQuantam;
                        t += timeQuantam;
                        System.out.print("P" + j + " (" + t + ") | ");
                    } else {
                        t += process[j].burstTime;
                        System.out.print("P" + j + " (" + t + ") | ");
                        process[j].burstTime = 0;
                        process[j].tat = t - process[j].arrivalTime;
                        process[j].waitingTime = t - process[j].copy_of_burstTime - process[j].arrivalTime;
                    }
                }
            }
        }
        int totalTurnAround = 0, totalWaiting = 0;
        for (int i = 1; i <= n; i++) {
            totalTurnAround += process[i].tat;
            totalWaiting += process[i].waitingTime;
        }
        float avg_turn = (float) totalTurnAround / n;
        float avg_wait = (float) totalWaiting / n;
        System.out.println();
        System.out.println("P\t TT \t WT");
        for (int m = 1; m <= n; m++) {
            System.out.println(process[m].pr + "\t" + process[m].tat + "\t" + process[m].waitingTime);
        }
        System.out.println("total waiting time : " + totalWaiting);
        System.out.println("average waiting time : " + avg_wait);
        System.out.println("\ntotal turn around time : " + totalTurnAround);
        System.out.println("avg turn around time : " + avg_turn);

    }

    public static void sjf(Process process[]) {
        double avgWaitingTime = 0.0, avgTurnAroundTime = 0.0;
        System.out.println("-------------------Gantt Chart----------------------");
        System.out.println(" ");
        for (int t = 0; t < sum;) {

            int min = 9999, index = 0;

            for (int j = 0; j < process.length; j++) {

                if (process[j].arrivalTime <= t && process[j].burstTime < min && process[j].burstTime > 0) {

                    min = process[j].burstTime;
                    index = j;

                }

            }
            // System.out.println(index);
            process[index].burstTime = 0;
            t += process[index].copy_of_burstTime;
            System.out.print("  |  " + process[index].id + " " + "(" + (t) + ")");

            process[index].turnAroundTime = t - process[index].arrivalTime;
            process[index].waitingTime = process[index].turnAroundTime - process[index].copy_of_burstTime;

        }
        System.out.println("");
        System.out.println("--------------------Table----------------------------");
        System.out.println("P\t TT \t WT");
        for (int i = 0; i < process.length; i++) {
            System.out.println(process[i].id + "\t" + process[i].turnAroundTime + "\t" + process[i].waitingTime);
            avgWaitingTime += process[i].waitingTime;
            avgTurnAroundTime += process[i].turnAroundTime;
        }
        System.out.println("Total waiting time: " + avgWaitingTime);
        System.out.println("average waiting time: " + (avgWaitingTime / process.length));
        System.out.println("Total turnaround time: " + avgTurnAroundTime);
        System.out.println("average turnaround time: " + (avgTurnAroundTime / process.length));
    }
    // ------------------------------------------------------------------------------------------------------------------
}