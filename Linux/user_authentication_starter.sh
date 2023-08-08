#!/bin/bash
## to be updated to match your settings
PROJECT_HOME="."
credentials_file="$PROJECT_HOME/data/credentials.txt"
archive_file="$PROJECT_HOME/data/archive.txt"
logged_in_user=""
# Function to prompt for credentials
get_credentials() {
    read -p 'Username: ' user
    read -rs -p 'Password: ' pass
    echo
}

generate_salt() {
    openssl rand -hex 8
    return 0
}

## function for hashing
hash_password() {
    # arg1 is the password
    # arg2 is the salt
    password=$1
    salt=$2
    # we are using the sha256 hash for this.
    echo -n "${password}${salt}" | sha256sum | awk '{print $1}'
    return 0
}

 # Task 1 assigned to Takudzwa
check_existing_username(){
    username=$1
    # fileDB

    # check if the DB file exists
    if [ -s  "$credentials_file" ]; then
        if grep -q "^$username:" "$credentials_file"; then
            echo "true"
        fi

    else
        print_error "File doen't exist"
    fi
    # echo "$username from register"
    ## verify if a username is already included in the credentials file
}

## function to add new credentials to the file
## Assigned to Julius
register_credentials() {
    # arg1 is the username
    # arg2 is the password
    # arg3 is the fullname of the user
    # arg4 (optional) is the role. Defaults to "normal"
    read -p "Enter username: " username
    read -s -p "Enter your password: " password
    echo ""
    read -p "Enter your fullname: " fullname
    role="normal"
    # username=$1
    # password=$2
    # fullname=$3
    # echo "$password"
    ## call the function to check if the username exists
    status=$(check_existing_username $username) 
    #TODO: if it exists, safely fails from the function.
    if [ "$status" = "true" ]; then
        print_error "A user with the username '$username' already exist"
        return 1
    fi
        ## retrieve the role. Defaults to "normal" if the 4th argument is not passed
        ## check if the role is valid. Should be either normal, salesperson, or admin
        ## first generate a salt
        salt=`generate_salt` 
        ## then hash the password with the salt
        hashed_pwd=`hash_password $password $salt`
        userData="$username:$hashed_pwd:$salt:$fullname:$role:0"    
        echo "$userData" >> "$credentials_file"
        ## append the line in the specified format to the credentials file (see below)
        ## username:hash:salt:fullname:role:is_logged_in
        # echo "doesn't"
}

# Function to verify credentials
# Assigned to Claude
print_error() {
    echo -e "\033[1;31mError: $@\033[0m"
}

verify_credentials() {
    ## arg1 is username
    ## arg2 is password
    username=$1
    password=$2
    echo "$username"
    ## retrieve the stored hash, and the salt from the credentials file
    # if there is no line, then return 1 and output "Invalid username"
   if [ ! -s "$credentials_file" ]; then
    print_error "Credentials file is empty."
    exit 1
   fi
    ## compute the hash based on the provided password
    
    ## compare to the stored hash
    ### if the hashes match, update the credentials file, override the .logged_in file with the
    ### username of the logged in user
    ### else, print "invalid password" and fail.

  
    let login_successful="false"
     status=$(check_existing_username $username) 
     if [ "$status" != "true" ]; then
     print_error "Invalid username \n"
     return 1
     fi
    while IFS=: read -r userName hashed_password salt fullname role status; do
        if [ "$userName" == "$username" ]; then
            local user_hashed_password=`hash_password $password $salt`
            if [ "$user_hashed_password" = "$hashed_password" ]; then
                login_successful="true"
                logged_in_user="$userName:$hashed_password:$salt:$fullname:$role"
                break
            else
                print_error "Invalid password\n"
                return 1
            fi
        fi
    
        
    done < "$credentials_file"
   
    if [ "$login_successful" = "true" ]; then
       
        os=$(uname)
        if [[ "$os" == "Darwin" ]]; then
        sed -i "" "/^$username:/ s/:$status\$/:1/" "$credentials_file"
        else
            sed -i "/^$username:/ s/:$status\$/:1/" "$credentials_file"
        fi
        echo "Login successfully!"
        return 0
    fi
    
}

## Assigned to Dieudonne
logout() {
    #TODO: check that the .logged_in file is not empty
    # if the file exists and is not empty, read its content to retrieve the username
    echo "Logging out"
    # of the currently logged in user
    if [ "$logged_in_user" != "" ]; then
        IFS=":" read -r username password salt fullname role <<< "$logged_in_user"
    # then delete the existing .logged_in file and update the credentials file by changing the last field to 0
        os=$(uname)
        if [[ "$os" == "Darwin" ]]; then
            sed -i "" "/^$username:/ s/:$status\$/:0/" "$credentials_file"
        else
            sed -i "/^$username:/ s/:$status\$/:0/" "$credentials_file"
        fi
    fi
    logged_in_user=""
    echo "Logged out successfully"
}

readUserInput(){
    username=$1
    echo "Choose the role to assing to the user $username:"
    echo "1. normal"
    echo "2. Salesperson"
    echo "3. Pharmacist"
    echo "4. admin"
    printf "Enter your choice :"
    read -r user_input
    echo 
  
}
updateRole(){
    user=$1
    use_role=$2
    status=$3
    local check=""
    check=$(check_if_user_is_admin "$role" "modify")
     if [ "$check" != "" ]; then
         echo $check
    fi
     if [ "$check" != "" ]; then
     return 1
     fi
    os=$(uname)
    if [[ "$os" == "Darwin" ]]; then
    sed -i "" "/^$user:/ s/:$role:$status\$/:$use_role:$status/" "$credentials_file"

    else
    sed -i  "/^$user:/ s/:$role:$status\$/:$use_role:$status/" "$credentials_file"
    fi
    echo "user account updated successfully"
}
check_if_user_is_admin(){
    role=$1
    action=$2
     if [ "$role" = "admin" ]; then
        print_error "Permission denied. you are not allowed to $action the system admin."
        return 1
    fi
}
read_user_info(){
          user=$1
          local user_info=""
           if grep -q "^$user:" "$credentials_file"; then
            while IFS=: read -r userName hashed_password salt fullname role status; do
            if [ "$userName" = "$user" ]; then
                 user_info="$userName:$hashed_password:$salt:$fullname:$role:$status"
                break
            fi
            done < "$credentials_file"
            fi
            echo "$user_info"
}

# Assigned to Claude
## Create the menu for the application
# at the start, we need an option to login, self-register (role defaults to normal)
# and exit the application.

# After the user is logged in, display a menu for logging out.
# if the user is also an admin, add an option to create an account using the 
# provided functions.

# Main script execution starts here
echo "Welcome to the authentication system."
while true; do
    echo "choose an option:"
    echo "1. to login"
    echo "2. to register"
    echo "3. to close the application"
    read -p "Enter your choice: " choice

    case "$choice" in
        1)
            get_credentials
            verify_credentials "$user" "$pass"
            while [ "$logged_in_user" != "" ]; do

                echo "Enter 4 to logout"
                IFS=":" read -r username password salt fullname role <<< "$logged_in_user"
                if [ "$role" = "admin" ]; then 
                    echo "Enter 5 to create a user"
                    echo "Enter 6 to delete a user account"
                fi
                    read -p "Enter your choice: " input
                if [ "$input" = 4 ]; then
                    logout
                elif [ "$input" = 5 ]; then
                    read -p "Enter username to create the account: " user
                   
                    validated_user_role_and_status=""
                    if ! grep -q "^$user:" "$credentials_file"; then
                         print_error "User $user not found"
                    else
                          while IFS=: read -r userName hashed_password salt fullname role status; do
                            if [ "$userName" = "$user" ]; then
                                validated_user_role_and_status="$role:$status"
                                break
                            fi
                          done < "$credentials_file"
                        choice_choosen="false"
                    #echo "$validated_user_role_and_status is found"
                    if [ "$validated_user_role_and_status" != "" ]; then
                        IFS=":" read -r role status <<< "$validated_user_role_and_status"
                        # echo "$role:$status"
                        while [ "$choice_choosen" = "false" ]; do
                        readUserInput "$user"
                        choice_choosen="false"
                        user_role="$user_input"
                            case "$user_role" in
                                1)
                                    updateRole "$user" "normal" "$status"
                                
                                choice_choosen="true"
                            ;;
                            2)
                                updateRole "$user" "salesperson" "$status"
                                choice_choosen="true"
                                ;;
                                3)
                                updateRole "$user" "pharmacist" "$status"
                                choice_choosen="true"
                                ;;
                        4)
                        updateRole "$user" "admin" "$status"
                            choice_choosen="true"
                                ;;
                            *) 
                            print_error "Invalid role"
                            ;;
                            esac
                        done
                            
                     fi
                  fi
                        
                elif [ "$input" = 6 ]; then
                 read -p "Enter username to delet the account: " user
                 if grep -q "^$user:" "$credentials_file"; then
                     user_info=$(read_user_info $user)
                     IFS=":" read -r userName hashed_password salt fullname role status <<< "$user_info"
                      is_admin_check=""
                      is_admin_check=$(check_if_user_is_admin "$role" "delete")
                      if [ "$is_admin_check" != "" ]; then
                          echo $is_admin_check
                      
                      else 
                        read -p "are you sure you want to delete $user's account? Enter y/yes and n/No " comfirm
                        if [ "$comfirm" = 'y' ]; then
                            IFS=":" read -r log_username log_password log_salt flog_ullname log_role <<< "$logged_in_user"
                             TIMESTAMP=`date +%Y-%m-%d_%H-%M-%S`
                             formerUserData="$userName:$hashed_password:$salt:$fullname:$role:0:$log_username:$TIMESTAMP" 
                            sed -i "" "/^$user:/d" "$credentials_file"
                            echo "Account is successfully deleted"
                            echo "$formerUserData" >> "$archive_file"
                        elif [ "$comfirm" = 'n' ]; then
                            echo "Canceled deleting"
                        else 
                        print_error "Invalid choice"
                        fi
                    fi
                 else
                    print_error "Username does not exist"
                 fi

                else
                 print_error "Invalid choice"
                fi
            done
            ;;
        2)
            register_credentials
            ;;
        3)
            echo "Exiting application..."
            exit 0
            ;;
    
        *)
            echo "Invalid choice. Please select a valid option."
            ;;
    esac
done





#Assigned to Julius
#### BONUS
#1. Implement a function to delete an account from the file

