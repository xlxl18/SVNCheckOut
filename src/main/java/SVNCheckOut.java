import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.wc.*;


import java.util.Date;
import java.io.File;
import java.util.EmptyStackException;

public class SVNCheckOut {

            public static void main(String[] args) {

            String userName="tima_e";
            //String userName=System.getProperty("user.name");
            String passWord="2F10d189";
            String repoUrl="https://192.168.4.8/svn/"+userName;
            String folder="/Users/tima/Documents/test";
            File desFolder = new File(folder);
            File[] files = desFolder.listFiles();
            SVNProperties prop = new SVNProperties();
            Logger logger = LoggerFactory.getLogger(SVNCheckOut.class);
            logger.info("SVNCheckOut");
            //for (File listOfFiles: files) {
               // System.out.println(listOfFiles);
            //}

            try {
                SVNURL url = SVNURL.parseURIEncoded(repoUrl);
                SVNRepository repo = SVNRepositoryFactory.create(url);
                //write to log file about creating of repo
                logger.info("The repository has beed created");
                //create authenication data
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, passWord);
                repo.setAuthenticationManager(authManager);
                logger.info("Repository ROOT: " + repo.getRepositoryRoot(true));
                logger.info("Repository UUID: " + repo.getRepositoryUUID(true));
                logger.info("Tha latest revision is: " + repo.getLatestRevision());
                //create client manager and set authentication
                SVNClientManager clientManager = SVNClientManager.newInstance();
                clientManager.setAuthenticationManager(authManager);
                logger.info("Auth manager was set");
                //SVNCommitClient commitClient = new SVNCommitClient(authManager,null);
                //commitClient.doCommit(files,false,"by "+userName,prop,null,false,true,SVNDepth.INFINITY);
                //Checking out the folder
                SVNUpdateClient updateClient = clientManager.getUpdateClient();
                updateClient.setIgnoreExternals(false);
                SVNRevision svnRevision = SVNRevision.create(repo.getLatestRevision());
                long doCheckout=updateClient.doCheckout(url, desFolder, svnRevision, svnRevision, SVNDepth.INFINITY, false);
                System.out.println(doCheckout);
                logger.info("Checked out the project from repository");

                //Add folder{
                //if (args[0].equals("import")) {
                    //ISVNEditor editor = repo.getCommitEditor("Test", null, true, null);
                    //editor.openRoot(-1);
                    //editor.addFile(folder, null, -1);
                    //logger.info("The " + desFolder + " folder was added");
                //}
                //SVNWCClient svnwcClient = clientManager.getWCClient();
               // svnwcClient.doAdd(desFolder,false,false,false,SVNDepth.INFINITY,false,false,false);


                //logger.info("Updating the repository");
                SVNCommitClient commitClient = clientManager.getCommitClient();
                commitClient.doImport(desFolder,url,"test",null,true,true,SVNDepth.INFINITY);
                SVNCommitInfo commitInfo=commitClient.doCommit(files,false,"commited in "+new Date(),null,null,false, true,SVNDepth.INFINITY);
                System.out.println(commitInfo.getNewRevision());
                //logger.info("New Rebvision "+commitInfo.getNewRevision());
                System.out.println((commitInfo.getNewRevision()));
            }
            catch (SVNException e) {
                //logger.info("There is an error during creating and exporting repository");
                //logger.info(e.getErrorMessage().toString());
                e.printStackTrace();
            }
            catch(NullPointerException e) {
                logger.info("Empty pointer");
            }
            catch(EmptyStackException e) {
                e.printStackTrace();
            }



        }

}
