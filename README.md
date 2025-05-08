# merged_task_and_wish_management
# Code Merging Process

In this project, the code written by three different developers was kept in separate branches and then manually merged via GitHub. Pull Requests were not used.

 Project Link: [https://github.com/cgokdere/merged_task_and_wish_management](https://github.com/cgokdere/merged_task_and_wish_management)

##  Contributors and Branches
- Ceren Gökdere → `ceren-branch`  
- Esma Çalış → `esma-branch`  
- Elif İstanbulluoğlu→ `elif-branch`  

##  Merging Steps

1. The project was cloned from GitHub:
   ```bash
   git clone https://github.com/cgokdere/merged_task_and_wish_management.git
   cd merged_task_and_wish_management
   ```

2. A separate branch was created for each developer, and they pushed their own code to their respective branches.

3. To start merging, we switched to the `main` branch.

4. Each branch was merged into the `main` branch one by one:
   ```bash
   git merge origin/ceren-branch  
   git merge origin/esma-branch  
   git merge origin/elif-branch  
   ```

5. If any merge conflicts occurred, they were resolved manually and committed.

6. Finally, the updated `main` branch was pushed back to GitHub.

##  Testing
- After merging, the code was tested successfully.
- It was confirmed that all features worked together properly.

##  Conclusion
The merging process was successfully completed, and the `main` branch is now up-to-date. Contributions from all developers have been integrated into the final version of the project.
