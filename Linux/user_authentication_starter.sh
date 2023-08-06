#!/bin/bash
## to be updated to match your settings
PROJECT_HOME="."
credentials_file="$PROJECT_HOME/data/credentials.txt"
IFS=' '
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
    fileDB=()
    # check if the DB file exists
    if [ -s  "$credentials_file" ]; then
        echo "Entered here"
        while read -r -a line; do  
            formattedline=${line##*( )}
            formattedline=${formattedline%%*( )}
            fileDB+=("$formattedline")
        done < "$credentials_file"
        for i in ${fileDB[@]}; do
            echo "entry - $i"
        done
    else
        echo "File doen't exist"
    fi
    echo "$username from register"
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
    # username=$1
    # password=$2
    # fullname=$3
    # echo "$password"
    ## call the function to check if the username exists
    check_existing_username $username 
    #TODO: if it exists, safely fails from the function.
    
    ## retrieve the role. Defaults to "normal" if the 4th argument is not passed

    ## check if the role is valid. Should be either normal, salesperson, or admin

    ## first generate a salt
    # salt=`generate_salt` --
    ## then hash the password with the salt
    # hashed_pwd=`hash_password $password $salt`--
    ## append the line in the specified format to the credentials file (see below)
    ## username:hash:salt:fullname:role:is_logged_in
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
    while IFS=: read -r userName hashed_password salt fullname role status; do
        if [ "$userName" != "$username" ]; then
            print_error "Invalid username \n"
            return 1
        fi
       
        local user_hashed_password=`hash_password $password $salt`
        if [ "$user_hashed_password" = "$hashed_password" ]; then
            login_successful="true"
            logged_in_user="$userName:$hashed_password:$salt:$fullname:$role"
            break
        else
            print_error "Invalid password\n"
            return 1
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
# logout() {
#     #TODO: check that the .logged_in file is not empty
#     # if the file exists and is not empty, read its content to retrieve the username
#     # of the currently logged in user

    # then delete the existing .logged_in file and update the credentials file by changing the last field to 0
#    echo "Logging out"
# }

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
            if [ "$logged_in_user" != "" ]; then
                echo "Enter 4 to logout"
                IFS=":" read -r username password salt fullname role <<< "$logged_in_user"
                if [ "$role" = "admin" ]; then 
                echo "Enter 5 to create a user"
                fi
                read -p "Enter your choice: " input
                if [ "$input" = 4 ]; then
                    logout
                elif [ "$input" = 5 ]; then
                     echo "Admin is about to create a user"
                else
                 print_error "Invalid choice"
                fi
            fi
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