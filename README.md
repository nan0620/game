# ITSD Card Game
JDK requires version 14.

Functional modular online card game developed using the Java programming language.
(Due to the time difference between most of the group members, our group used offline programming and uploaded to Gitlab in the final integration, so please forgive us.)

Phased tasks

Week 1 (31th May)
1. Deploy the relevant environment required for the project.
2. Learn, download and run the Card Game Code Template on Moodle Page.
3. Understand the basic usage knowledge of Git and register for GitLab.
4. Review the basic knowledge of Java.
5. Discuss this week's Q&A on Teams.
6. Finish this week's workload.

Week 2 (6th June)
1. Dig deeper into the meaning of the template code.
2. Finish this week's workload.
3. Discuss this week's Q&A on Teams.

Week 3 (14th June)
1. Extend existed classes and methods.
2. Add new useful classes and methods.
3. Finish this week's workload.
4. Discuss this week's Q&A on Teams.

Week 4 (21th June)
1. Learn Juit Test.
2. Finish this week's workload.
3. Discuss this week's Q&A on Teams.

Week 5 (28th June)
1. Finish this week's workload.
2. Discuss the AI logic of its basic operations, like deploying, moving, attacting
3. Discuss this week's Q&A on Teams.

Week 6 (5th July)
1. All story cards were reconsidered and re-coded to improve the system.
2. Test all the functions, and check the bugs.
3. Write the Sprint reports.
4. Discuss this week's Q&A on Teams.
5. Finish this week's workload

Workload
Taking the story card provided on moodle as a reference, based on the actual development logic process, and taking into account the priority and cost, the project division of workload is carried out as below.

Week 1 (31th May)
 Story Card Serial Number	Code Writer
 1-5 and 33	                Tianshan and Yuntao
 13	                        Xinlei
 14	                        Tianshan
 15	                        Yuntao
 Cost：15

Week 2 (6th June)
 Story Card Serial Number	Code Writer
 29-32                          Tianshan          
 23                             Tianshan
 Cost：15

Week 3 (14th June)
 Story Card Serial Number	Code Writer
 16-19	                        Nan 
 21	                        Nan
 Cost：19

Week 4 (21th June)
 Story Card Serial Number	Code Writer
 24-28	                        Qitao
 Cost：21

Week 5 (28th June)
 Story Card Serial Number	Code Writer
 8-12	                        Xinlei
 Cost：17

Week 6 (5th July)
 Story Card Serial Number	Code Writer
 6-7	                        Yuntao
 20 and 22	                Yuntao
 Cost：16

Bugs still existed
1. After each turn, the value of mana will not set as zero. It shows the left value of mana.
2. The avator can bypass other cards that are blocked in its effective path to move or attack.
3. When using a magic card that directly reduces 2 points of life, the avator card that can be used on the board should be marked in red, but we do not realise that function yet.
4. After the game ending, the system will not automatically shut down.
5. The game has a small chance of crashing mid-run.
6. There is no turn timer and the pile of cards cannot be displayed.
7. There is no bright light when you select a usable card.

Weekly Retrospect

Week 1 (31th May)（Organised by Nan）
1. Figure out IntelliJ IDEA and Eclipse how to import sbt project.
2. The template front-end code cannot run on Java 16, "sbt compile" may because use the wrong version of Java.
3. CMD VS PowerShell, PowerShell wins.
4. About test class, tentatively, it is marked annotation in the code and no additional test code is written.
5. Discuss the possibility of a program getting stuck mid-run and needing to be restarted. Find out what the problem is and optimise it.

Week 2 (6th June)（Organised by Yuntao)
1. No need to edit the javascript file for the project.
2. Check the project structure if meeting some errors while running the template code.
3. Discuss how the main unit card's attacks and healing are updated in real time.

Week 3 (14th June)（Organised by Tianshan)
1. Better run "sbt clean" and check if the port 9000 has been occupied before run "sbt run" in MacOS intellJ IDE.
2. Better solution about issue 1 is to use CTRL+D to stop AkkaHttpServer rather than CTRL+C. CTRL+C is not trully stop the sbt server, next time run "sbt run" will show "9000 port is on use" error.
3. Every time we insert a new class, we need to re-compile before re-run.
4. When figuring out where a unit should move to, because the UI does will always move a unit horizontally then vertically, this can cause a unit to visually move through an enemy unit instead of taking a clear path. About this bug, we need further discuss ad research.

Week 4 (21th June)（Organised by Xinlei)
1. Collate and debug the code from the first two weeks and make functional changes.
2. Spell hand effect implementation and card removal from hand.
3. Discuss increasing blood cards and how to achieve simultaneous increase of units and players.
4. Discuss how to implement additional attributes for unit cards.
5. When checking the function completed in the early stage, it was found that when the card was drawn, the last card partially overlapped with the end-round button.

Week 5 (28th June)（Organised by Qitao)
1. Many times modified and tested, basically realize the effect of the card.
2. AI operation during the player can not be operated, the root of the card corresponding properties to play.
3. Discussion, the turn time problem, the current player turn, there is no time limit, later need to be optimized.

Week 6 (5th July)（Organised by all members)
1. Discuss code functions, integrate all code and add comments.
2. Test the system, look for problems and find and modify them.
3. Integrate weekly meetings.
4. Try juit testing, meet problems and discuss them.
5. When figuring out where a unit should move to, because the UI does will always move a unit horizontally then vertically, this can cause a unit to visually move through an enemy unit instead of taking a clear path. About this bug, we need further discuss ad research.
6.  When trying to repeatedly highlight tiles, it shows error message indicating overflow. About that issue, add a short Thread.sleep between each call of the drawTile command (say 30ms).
7. Discuss the implementation of the function where a bright light appears when you click on a usable card.
