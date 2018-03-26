import org.apache.commons.lang.ArrayUtils;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.util.EmptyStackException;
import java.util.List;

public class SVNCheckOut {

    public static void main(String[] args) {

        //String userName = "tima_e";
        String userName=System.getProperty("user.name");
        String passWord = "2f10d189";
        String repoUrl = "https://192.168.4.8/svn/" + userName;
        //String folder = "/Users/tima/Documents/test";
        String folder = "C:\\docs";
        File desFolder = new File(folder);
        File svnFolder = new File(folder+"\\.svn");
        File jarFolder =new File ("C:\\scripts");
        //File[] files = desFolder.listFiles();
        List<File> addUnverFiles=new ArrayList<File>();
        int numElem=-1;
        //List<File> addModifFiles=new ArrayList<File>();
        Logger logger = LoggerFactory.getLogger(SVNCheckOut.class);
        //logger.info("SVNCheckOut");
        jarFolder.mkdir();
        if (desFolder.exists() == false) {
            desFolder.mkdir();
        }
        File[] files = desFolder.listFiles();
        for(int i=0; i<files.length;i++) {
            if(files==null) {
               logger.info("Empty array");
           }
            //System.out.println(files[i].getPath());
            else if(files[i].getPath().contains("\\.svn") || files[i].getPath().contains("/.svn")) {
                numElem=i;
                break;
            }
        }
        if(numElem!=-1) {
            files = (File[]) ArrayUtils.removeElement(files, files[numElem]);
        }

        try {
            SVNURL url = SVNURL.parseURIEncoded(repoUrl);
            SVNRepository repo = SVNRepositoryFactory.create(url);
            ISVNOptions options = SVNWCUtil.createDefaultOptions(true);

            //write to log file about creating of repo
            logger.info("The repository has beed created");

            //create authentication data
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, passWord);
            repo.setAuthenticationManager(authManager);
            logger.info("Repository ROOT: " + repo.getRepositoryRoot(true));
            logger.info("Repository UUID: " + repo.getRepositoryUUID(true));
            logger.info("Tha latest revision is: " + repo.getLatestRevision());

            //create client manager and set authentication
            SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
            logger.info("Auth manager was set");

            //Checking out the folder


            if (svnFolder.exists()==false) {
               SVNUpdateClient updateClient = clientManager.getUpdateClient();
               updateClient.setIgnoreExternals(false);
               SVNRevision svnRevision = SVNRevision.create(repo.getLatestRevision());
               long doCheckout = updateClient.doCheckout(url, desFolder, svnRevision, svnRevision, SVNDepth.INFINITY, false);
               System.out.println(doCheckout);
               logger.info("Checked out the project from repository");
            }


            //Create svnwcclient to manage our Working Copy

                SVNWCClient svnwcClient = new SVNWCClient(authManager, options);
                SVNStatusClient fileStatus = new SVNStatusClient(authManager, options);
                SVNCommitClient commitClient = clientManager.getCommitClient();
                for (File listF : files) {
                    //SVNStatus svnStatus = fileStatus.doStatus(listF, false);
                    //if (!svnStatus.isVersioned()) {
                     //
                     //  addUnverFiles.add(listF);
                     //  logger.info("The " + listF + " file was added");
                   //} else {
                    addUnverFiles.add(listF);

                }




                //Transform List to Arrays and add Files from Working Copy to SVN
                File[] filesForCom = addUnverFiles.toArray(new File[addUnverFiles.size()]);

                if (filesForCom.length != 0) {
                    svnwcClient.doAdd(filesForCom, true, false, false, SVNDepth.INFINITY, false, false, false);
                    commitClient.doCommit(filesForCom, false, "Commited in " + new Date(), null, null, false, true, SVNDepth.INFINITY);
                }

                //if (filesForComm.length != 0) {
                 //   svnwcClient.doAdd(filesForComm, true, false, false, SVNDepth.INFINITY, false, false, false);
                 //   commitClient.doCommit(filesForComm, false, "Commited in " + new Date(), null, null, false, true, SVNDepth.INFINITY);
               // }
                //svnwcClient.doAdd(filesForComm, true, false, false, SVNDepth.INFINITY, false, false, false);
                logger.info("Commited by " + userName);



        }

            catch(SVNException e){
                logger.info("There is an error during creating and exporting repository");
                logger.info(e.getErrorMessage().toString());
                e.printStackTrace();
            }

            catch(NullPointerException e){
                logger.info("Empty pointer");

            }

            catch(EmptyStackException e){
                e.printStackTrace();
            }


        }

    }

