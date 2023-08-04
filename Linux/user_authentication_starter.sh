#!/bin/bash
## to be updated to match your settings
PROJECT_HOME="."
credentials_file="$PROJECT_HOME/data/credentials.txt"

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

check_existing_username(){
    username=$1
    ## verify if a username is already included in the credentials file
}

## function to add new credentials to the file
register_credentials() {
    # arg1 is the username
    # arg2 is the password
    # arg3 is the fullname of the user
    # arg4 (optional) is the role. Defaults to "normal"

    username=$1
    password=$2
    fullname=$3
    ## call the function to check if the username exists
    check_existing_username $username
    #TODO: if it exists, safely fails from the function.
    
    ## retrieve the role. Defaults to "normal" if the 4th argument is not passed

    ## check if the role is valid. Should be either normal, salesperson, or admin

    ## first generate a salt
    salt=`generate_salt`
    ## then hash the password with the salt
    hashed_pwd=`hash_password $password $salt`
    ## append the line in the specified format to the credentials file (see below)
    ## username:hash:salt:fullname:role:is_logged_in
}

# Function to verify credentials
verify_credentials() {
    ## arg1 is username
    ## arg2 is password
    username=$1
    password=$2
    ## retrieve the stored hash, and the salt from the credentials file
    # if there is no line, then return 1 and output "Invalid username"

    ## compute the hash based on the provided password
    
    ## compare to the stored hash
    ### if the hashes match, update the credentials file, override the .logged_in file with the
    ### username of the logged in user

    ### else, print "invalid password" and fail.
}

logout() {
    #TODO: check that the .logged_in file is not empty
    # if the file exists and is not empty, read its content to retrieve the username
    # of the currently logged in user

    # then delete the existing .logged_in file and update the credentials file by changing the last field to 0
}

## Create the menu for the application
# at the start, we need an option to login, self-register (role defaults to normal)
# and exit the application.

# After the user is logged in, display a menu for logging out.
# if the user is also an admin, add an option to create an account using the 
# provided functions.

# Main script execution starts here
echo "Welcome to the authentication system."

#### BONUS
#1. Implement a function to delete an account from the file