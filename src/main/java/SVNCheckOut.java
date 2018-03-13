import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.wc.*;
import org.tmatesoft.svn.core.wc2.SvnWorkingCopyInfo;

import java.util.Date;
import java.io.File;
import java.util.EmptyStackException;

public class SVNCheckOut {

            public static void main(String[] args) {

            String userName="tima_e";
            //String userName=System.getProperty("user.name");
            String passWord="2F10d189";
            String repoUrl="https://192.168.4.8/svn/"+userName;
            File desFolder = new File("/Users/tima/Documents/test");
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
                ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName,passWord);
                repo.setAuthenticationManager(authManager);
                logger.info("Repository ROOT: "+repo.getRepositoryRoot(true));
                logger.info("Repository UUID: "+repo.getRepositoryUUID(true));
                logger.info("Tha latest revision is: " + repo.getLatestRevision());
                //create client manager and set authentication
                SVNClientManager clientManager = SVNClientManager.newInstance();
                clientManager.setAuthenticationManager(authManager);
                logger.info("Auth manager was set");
                //SVNCommitClient commitClient = new SVNCommitClient(authManager,null);
                //commitClient.doCommit(files,false,"by "+userName,prop,null,false,true,SVNDepth.INFINITY);
                //Checking out the folder
                SVNUpdateClient updateClient=clientManager.getUpdateClient();
                updateClient.setIgnoreExternals(false);
                updateClient.doCheckout(url,desFolder,SVNRevision.create(repo.getLatestRevision()),SVNRevision.create(repo.getLatestRevision()),SVNDepth.INFINITY,false);
                logger.info("Checked out the project from repository");

                //Add folder
                ISVNEditor editor = repo.getCommitEditor("Test",null,true,null);
                editor.addDir("/Users/tima/Documents/test",null,-1);
                //SVNWCClient svnwcClient = clientManager.getWCClient();
                //.doAdd(files,false,false,false,SVNDepth.INFINITY,false,false,false);
                logger.info("The "+desFolder+ " folder was added");

                //logger.info("Updating the repository");
                SVNCommitClient commitClient = clientManager.getCommitClient();
                commitClient.doCommit(files,false,"by "+userName,prop,null,false,true,SVNDepth.INFINITY);
                logger.info("Commited by User: "+userName);
            }
            catch (SVNException e) {
                logger.info("There is an error during creating and exporting repository");
            }
            catch(NullPointerException e) {
                logger.info("Empty pointer");
            }
            catch(EmptyStackException e) {
                e.printStackTrace();
            }


        }

}
