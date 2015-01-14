##CIS 457 Project 1: Mini HTTP Server

+ [Project Requirements](http://www.cis.gvsu.edu/~kalafuta/cis457/w15/labs/457prj1.html)
+ [How to Contribute](#how-to-contribute)
+ [Tips](#tips)

How to Contribute
-------------------
1. Fork the main repository to your github account using the link in the upper right.
2. If you don't have git set up on your own computer, use a lab computer(ssh or physical). It's probably best to use a lab computer to compile.
3. Change into the directory you want the httpserver directory copied.
4. Clone the repository you forked in step 1 to the current directory. 

    ```
    $ git clone https://github.com/<your username>/httpserver.git
    ```
	- OR (This method requires you have ssh set up with your github account)
	
	```
    $ git clone git@github.com:<your username>/httpserver.git
    ```
	
	 - To verify:
	 
	  ```
       $ ls 
      ```
		- all project files should now be in your working directory
		
5. Add the main repository to your remote repositories. Call it whatever you want, but this document will refer to it as `upstream`.

    ```
    $ git remote add upstream https://github.com/cjnz457/httpserver.git
    ```
6. You should now have two remotes associated with the repository in your working directory: `upstream` and `origin`. `origin` is automatically created when you clone a repository. 
    - To verify: 

        ```
       $ git remote -v
        ```
		
		- `upstream` should be the URL to the main repository and `origin` should be the URL to your forked repository.
		
7. You're now ready to make changes. It's good practice to switch to a new [branch](http://nvie.com/posts/a-successful-git-branching-model/) when you start working on a new feature.
    - To get a list of branches: (The current branch will be marked with an asterisk)

        ```
       $ git branch
        ```
    - To create a new branch:  

        ```
       $ git branch <branch name>
        ```
    - To switch branches:  

        ```
       $ git checkout <branch name>
        ```
    - To create a new branch and switch to it:  

        ```
       $ git checkout -b <branch name>
        ```

8. When you've completed a change, it's time to commit. 

    ```
    $ git commit -am "A short message that describes the change"
    ```
    - If you add a file, run this command first:
    
      ```
     $ git add --all
      ```
9. When you've completed and tested a feature and want to keep it, switch to your master branch, then merge your feature branch into `master` (make sure all changes have been committed). Finally, delete the feature branch. If you don't want to keep the changes, just delete the feature branch: 

    ```
    $ git checkout master
    $ git merge <branch name>
    $ git branch -d <branch name>
    ```
10. You may now want to contribute your changes to the main repository. 
    1. Fetch any changes that may have been applied to the main repository. Merge the main repository's master branch into your local master branch (make sure you are currently on your master branch) then resolve any conflicts (it will tell you if there are conflicts):
        
      ```
      $ git fetch upstream
      $ git merge upstream/master
      ```
    2. After a successful merge, push up to your forked repository on github:
    
      ```
      $ git push origin master
      ```
      Or, if you have multiple branches and you want to push all of them at once:
          
      ```
      $ git push --all origin
      ```
11. Now in github, create a pull request. When your pull request has been accepted, your changes will have been added to the main repository. 

## Tips
+ Do step 10 periodically to make sure your forked repository and working directory are up to date with the main repository
+ When making small changes that you know you're going to keep, it's pretty unnecessary to work on a different branch

