#!/bin/bash
set +x
systemctl --user status
systemctl --user stop webchat.service
 #          ssh production "rm -rf TicTacToe"
 #          ssh production "git clone https://github.com/BudDavis/TicTacToe"
 #          ssh production "cd TicTacToe;mvn clean compile package"
           # % git clone https://github.com/utastudents/cse3310_su23_group_3 
           # % ssh su23_group3@cse3310.org "systemctl --user stop webchat.service" 
           # scp -r cse3310_su23_group_3 su23_group3@cse3310.org:. 
           # % ssh su23_group3@cse3310.org "cd cse3310_su23_group_3;cd demo;mvn clean#;mvn
           # sysstemctl --user start webchat.service" 
           # %  ssh su23_group3@cse3310.org "systemctl --user status webchat.service" 
           
