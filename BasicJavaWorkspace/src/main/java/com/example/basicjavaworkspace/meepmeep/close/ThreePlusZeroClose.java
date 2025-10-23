                                                                            package com.example.basicjavaworkspace.meepmeep.close;

                                                                            import com.acmerobotics.roadrunner.Pose2d;
                                                                            import com.acmerobotics.roadrunner.Vector2d;
                                                                            import com.noahbres.meepmeep.MeepMeep;
                                                                            import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
                                                                            import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

                                                                            public class ThreePlusZeroClose {


                                                                                // The class name now matches the original filename

                                                                                public static void main(String[] args) {
                                                                                    MeepMeep meepMeep = new MeepMeep(800);

                                                                                    RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                                                                                            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                                                                            .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                                                                                            .setDimensions(18, 18)
                                                                                            .build();

                                                                                    myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-37, -53, Math.toRadians(90)))
                                                                                            .strafeToLinearHeading(new Vector2d(-15.5, -15.5), 180)


                                                                                            .build());

                                                                                    meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                                                                                            .setDarkMode(true)
                                                                                            .setBackgroundAlpha(0.95f)
                                                                                            .addEntity(myBot)
                                                                                            .start();
                                                                                }
                                                                            }

