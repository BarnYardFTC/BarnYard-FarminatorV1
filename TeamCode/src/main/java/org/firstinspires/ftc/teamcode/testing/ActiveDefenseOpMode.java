package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

@TeleOp(name="ActiveDefenseOpMode")
public class ActiveDefenseOpMode extends LinearOpMode {

    private final String MY_LAPTOP_IP = "192.168.43.104";
    private final int PORT = 5000;

    // List to keep track of everyone we have kicked
    private final ArrayList<String> logOfKickedIPs = new ArrayList<>();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Aegis Shield Initialized");
        telemetry.update();

        waitForStart();

        startActiveDefense();

        while (opModeIsActive()) {
            // Keep the telemetry updated with the full list
            telemetry.addLine("=== SECURITY LOG (KICKED INTRUDERS) ===");
            if (logOfKickedIPs.isEmpty()) {
                telemetry.addLine("No intruders detected yet.");
            } else {
                for (String ip : logOfKickedIPs) {
                    telemetry.addLine("🚨 KICKED: " + ip);
                }
            }
            telemetry.addLine("=======================================");
            telemetry.update();

            sleep(500); // Refresh display every half second
        }
    }

    private void startActiveDefense() {
        new Thread(() -> {
            while (opModeIsActive()) {
                try {
                    String subnet = "192.168.43";
                    for (int i = 2; i < 255; i++) {
                        String host = subnet + "." + i;

                        if (
//                                host.equals(MY_LAPTOP_IP) ||
                                host.equals("192.168.43.1")) continue;

                        if (isDevicePresent(host)) {
                            // If it's a new intruder, add to log and kick
                            if (!logOfKickedIPs.contains(host)) {
                                logOfKickedIPs.add(host);
                                kickUser(host);
                            }
                        }
                    }
                } catch (Exception e) { }
                sleep(2000);
            }
        }).start();
    }

    private boolean isDevicePresent(String ip) {
        try {
            // Method 1: Try a standard ping (500ms timeout)
            if (InetAddress.getByName(ip).isReachable(500)) return true;

            // Method 2: Try to "knock" on a common Windows port (135 is usually open)
            try (Socket socket = new Socket()) {
                socket.connect(new java.net.InetSocketAddress(ip, 135), 100);
                return true;
            } catch (IOException e) {
                // Port is closed or filtered
            }
        } catch (Exception e) { }
        return false;
    }

    private void kickUser(String ip) {
        new Thread(() -> {
            try {
                // Try running WITHOUT 'su' first, as some Hubs allow 'ip' commands
                // in the standard shell but block the 'su' binary.
                String[] cmd = {"ip", "neigh", "del", ip, "dev", "wlan0"};

                Process p = Runtime.getRuntime().exec(cmd);

                // Capture the error stream to see why it failed
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(p.getErrorStream())
                );
                String line = reader.readLine();

                int exitCode = p.waitFor();

                synchronized(logOfKickedIPs) {
                    if (exitCode == 0) {
                        logOfKickedIPs.add("✅ SUCCESS: " + ip);
                    } else {
                        // This will show you the ACTUAL reason (e.g., "Permission Denied")
                        logOfKickedIPs.add("⚠️ FAIL: " + ip + " (" + (line != null ? line : "Code " + exitCode) + ")");
                    }
                }
            } catch (Exception e) {
                synchronized(logOfKickedIPs) {
                    logOfKickedIPs.add("❌ CRASH: " + ip + " (" + e.getMessage() + ")");
                }
            }
        }).start();
    }

    public void sendDataToLaptop(String message) {
        new Thread(() -> {
            try (Socket socket = new Socket(MY_LAPTOP_IP, PORT)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
            } catch (Exception e) { }
        }).start();
    }
}