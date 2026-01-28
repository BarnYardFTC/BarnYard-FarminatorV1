package com.example.basicjavaworkspace.meepmeep.blue.far;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;



public class Blue_Far_ThreePlusSix   {


    public static double START_POSE_X = 60;
    public static double START_POSE_Y = -15;
    public static double START_HEADING = Math.toRadians(180);

    public static double SHOOTING_POSE_X = 55;
    public static double SHOOTING_POSE_Y = -10;
    public static double SHOOT_HEADING = Math.toRadians(204);
    public static double SHOOT_HEADING2 = Math.toRadians(205);

    public static double PRECOLLECT_Y = -33;
    public static int    SHOOTING_TIME_MS = 2000;

    public static double COLLECT_POSE_X = 52;
    public static double COLLECT_POSE2_X =60;
    public static double COLLECT_POSE_Y = -53;
    public static double COLLECT_HEADING = Math.toRadians(270);

    public static double RIGHT_COLLECT_POSE_X = 32;

    public static double SOUTH_READY_POSE_Y = 0;
    public static double SOUTH_COLLECT_POSE_Y = -53;



    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13.157, 18.03044)
                .build();


        TrajectoryActionBuilder startToShoot = myBot.getDrive().actionBuilder(
                        new Pose2d(START_POSE_X, START_POSE_Y, START_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING2);


        TrajectoryActionBuilder angleCollect = myBot.getDrive().actionBuilder(new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,PRECOLLECT_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,COLLECT_POSE_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,PRECOLLECT_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE_X,COLLECT_POSE_Y ),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,PRECOLLECT_Y),COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(COLLECT_POSE2_X,COLLECT_POSE_Y ),COLLECT_HEADING );

        TrajectoryActionBuilder angleToShoot = myBot.getDrive().actionBuilder(
                        new Pose2d(COLLECT_POSE2_X, COLLECT_POSE_Y,COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);

        TrajectoryActionBuilder rightCollect = myBot.getDrive().actionBuilder(
                        new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_READY_POSE_Y), COLLECT_HEADING )
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y), COLLECT_HEADING, new TranslationalVelConstraint(30));

        TrajectoryActionBuilder rightToShoot   = myBot.getDrive().actionBuilder(
                        new Pose2d(RIGHT_COLLECT_POSE_X, SOUTH_COLLECT_POSE_Y, COLLECT_HEADING))
                .strafeToLinearHeading(new Vector2d(SHOOTING_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);

        TrajectoryActionBuilder finalPos   = myBot.getDrive().actionBuilder(
                        new Pose2d(SHOOTING_POSE_X, SHOOTING_POSE_Y, SHOOT_HEADING))
                .strafeToLinearHeading(new Vector2d(RIGHT_COLLECT_POSE_X, SHOOTING_POSE_Y), SHOOT_HEADING);



        // Run the trajectory
        myBot.runAction(
                new SequentialAction(
                startToShoot.build(),
                angleCollect.build(),
                angleToShoot.build()
//                path4.build(),
//                path5.build()
        ));

        // MeepMeep visualization
        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}


