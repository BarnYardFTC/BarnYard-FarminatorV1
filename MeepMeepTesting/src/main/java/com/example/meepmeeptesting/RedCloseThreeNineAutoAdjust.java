package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RedCloseThreeNineAutoAdjust {


    public static final Pose2d startPose = new Pose2d( -49.333, 45.5, Math.toRadians(360 - 225));
    public static final Pose2d shootPose = new Pose2d(-23, 22, Math.toRadians(360 - 224));
    public static final Pose2d lastShootPose = new Pose2d(-36, 5, Math.toRadians(360 - 240));


    public static final Pose2d leftCollectPose = new Pose2d(-11.5, 58, Math.toRadians(360 - 270));
    public static final Pose2d midCollectPose = new Pose2d(12, 72, Math.toRadians(360 - 270));
    public static final Pose2d rightCollectPose = new Pose2d(35.5, 72, Math.toRadians(360 - 270));


    public static final Pose2d leftLoadZoneReady = new Pose2d(-11.5, 20, Math.toRadians(360 - 270));
    public static final Pose2d rightLoadZoneReady = new Pose2d(12, 20, Math.toRadians(360 - 270));
    public static final Pose2d midLoadZoneReady = new Pose2d(34.5, 20, Math.toRadians(360 - 270));

    public static final Pose2d parkPose = new Pose2d(-40, 22, Math.toRadians(360 - 227));
    public static final Pose2d gateOpenPose = new Pose2d(0, 70, Math.toRadians(360 - 180));
    public static final Pose2d gateCollectPose = new Pose2d(10,57, Math.toRadians(360 - 227));

    TranslationalVelConstraint fastToShoot = new TranslationalVelConstraint(150);

    public static void main(String[] args) {

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(17, 17)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(startPose)
                .strafeToLinearHeading(shootPose.component1(),shootPose.component2())

                .splineToLinearHeading(leftCollectPose, new Rotation2d(-0.5, 3))
                .setTangent(new Rotation2d(2,-2))
                .splineToLinearHeading(gateOpenPose, Math.toRadians(90))

                .strafeToLinearHeading(shootPose.component1(),shootPose.component2())

                .setTangent(new Rotation2d(2.1,-1.5))
                .splineToLinearHeading(midCollectPose, Math.toRadians(90))

                .setTangent(new Rotation2d(0,1))
                .splineToLinearHeading(shootPose, Math.toRadians(-150))

                .setTangent(new Rotation2d(2.1,-0.7))
                .splineToLinearHeading(rightCollectPose, new Rotation2d(0, 1.5))

                .setTangent(Math.toRadians(-120))
                .splineToLinearHeading(lastShootPose, Math.toRadians(198))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }



}
