
alias rs='source ~/.bashrc'
alias rse='exec bash'
alias ec='ge ~/.bashrc'
alias ecc='ge /sw/tools/bin/bashextras.sh'
alias gou='go /sw/code'
alias h='history'

alias ll='ls -l'
alias lla='ls -la'
alias lld='ls -ld'

alias gitdi='git difftool -y'
alias gitci='git commit -m '
alias gitad='git add'
alias gitst='git status -s'

export HISTTIMEFORMAT="%04y-%02m-%02d %02H:%02M:%02S "
export PS1='\[\e]0;\w\a\]\n\u@\h \w\n[\d \t] $ '
export PS2='> '
export PS4='+ '

export PATH=$PATH:/media/sf_vmfiles/tools/bin

function eclipse()
{
    ( /opt/eclipse/eclipse $* 1>/dev/null 2>&1 ) &
}

function na()
{
    ( nautilus $* 1>/dev/null 2>&1 ) &
}

function ge()
{
    ( gedit $* 1>/dev/null 2>&1 ) &
}

function gew()
{
    ge `which $1`
}

function go()
{
    todir=$1

    if (( "$#" > 1 )) ; then
        echo "ERROR: go takes only either 0 or 1 arguments"
        return
    elif (( "$#" == 0 )) ; then
        todir=$HOME
    fi
    
    if [[ ! -d $todir ]] ; then
        echo "ERROR: $1 is not a directory"
        return
    fi

    cd $1
    ls
}

function up()
{
    upcnt=0
    
    if [[ -z "$#" ]] ; then
        echo "ERROR: How'd you even do that?"
        return
    elif (( "$#" < 1 )) ; then
        upcnt=1
    else
        upcnt=$1
    fi

    ups=""
    for i in `seq 1 $upcnt` ; do
        ups="$ups../"
    done

    go $ups
}
