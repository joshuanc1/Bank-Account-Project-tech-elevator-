package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.TenmoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/users")
public class TenmoController {

    private UserDao userDao;
    private TenmoService tenmoService;
    private TransferDao transferDao;
    private AccountDao accountDao;

    public TenmoController(UserDao userDao, TenmoService tenmoService, TransferDao transferDao, AccountDao accountDao){

        this.userDao = userDao;
        this.tenmoService = tenmoService;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    /**
     *
     * Lists all users, except the current user.
     *
     * Allows search by username (optional).
     *
     * */
    @RequestMapping(method = RequestMethod.GET)
    public List<User> listAllUsers(@RequestParam(defaultValue = "") String username, Principal principal){

        List<User> userList = new ArrayList<>();

        try {
            if (!username.equals("")) {
                userList.add(userDao.findByUsername(username));
            }
            else {

                userList = userDao.findAll();

                for(User user: userList){

                    if (user.getUsername().equalsIgnoreCase(principal.getName())){

                        userList.remove(user);

                    }

                }

            }
        }catch (Exception ex){

            System.out.println(ex.getMessage());

        }

        return userList;
    }

    /**
     *
     * Retrieve user by userID, only available to Admin role
     *
     * */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable int id){
        User user = null;
        try{
            user = userDao.getUserById(id);
        } catch(Exception e){
            System.out.println(e.getMessage());
    }
    return user;
}

    /**
     *
     * Retrieves account balance for current logged in user.
     *
     * */
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal principal){
        String username = principal.getName();
        return tenmoService.getBalanceByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path ="/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer) {
        Transfer newTransfer = null;

        transfer = tenmoService.changeUserIdToAccountID(transfer);

        if(tenmoService.compareAccountBalanceToTransferAmount(transfer)) {

            try {
                newTransfer = transferDao.createNewTransfer(transfer);

                tenmoService.updateAccountBalance(newTransfer);
                newTransfer = transferDao.getTransferByTransferID(newTransfer.getTransferID());

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return newTransfer;
    }

    /**
     *
     * Retrieve a transfer by the ID passed in by the user, if user is either sender or receiver
     *
     * */
    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferByID(@PathVariable(name = "id") int transferID, Principal principal){

        String username = principal.getName();

        return tenmoService.getTransferByIDOnlyIfSenderOrReceiver(transferID, username);

    }


    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public String getUsernameByAccountId(@PathVariable(name = "id") int accountID){
        return tenmoService.getUsernameSenderOrReciever(accountID);
    }


    /**
     * List all transfers for current user.
     * */
    @RequestMapping(path= "/transfer/getall", method = RequestMethod.GET)
    public List<Transfer> getAllTransactionsForUser(Principal principal){
       String username = principal.getName();
       return tenmoService.getAllTransfersByUser(username);
    }

    @RequestMapping(path = "/transfer/senderreceiver", method = RequestMethod.GET)
    public List<String> getTransferFromAndTo(@RequestBody Transfer transfer){

        List<String> toAndFrom = new ArrayList<>();

        toAndFrom.add(0, userDao.getUserByAccountID(transfer.getAccountTo()));
        toAndFrom.add(1, userDao.getUserByAccountID(transfer.getAccountFrom()));

        return toAndFrom;
    }



}
