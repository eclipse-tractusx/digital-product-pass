<!-- 
  Tractus-X - Digital Product Passport Application 
 
  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->


# Contributing to Eclipse Tractus-X

Thanks for your interest in this project.

## Project description

The companies involved want to increase the automotive industry's
competitiveness, improve efficiency through industry-specific cooperation and
accelerate company processes through standardization and access to information
and data. A special focus is also on SMEs, whose active participation is of
central importance for the network's success. That is why Catena-X has been
conceived from the outset as an open network with solutions ready for SMEs,
where these companies will be able to participate quickly and with little IT
infrastructure investment. Tractus-X is meant to be the PoC project of the
Catena-X alliance focusing on parts traceability.

* https://projects.eclipse.org/projects/automotive.tractusx

## Developer resources

Information regarding source code management, builds, coding standards, and
more.

* https://projects.eclipse.org/projects/automotive.tractusx/developer

The project maintains the source code repositories in the following GitHub organization:

* https://github.com/eclipse-tractusx/

## Eclipse Development Process

This Eclipse Foundation open project is governed by the Eclipse Foundation
Development Process and operates under the terms of the Eclipse IP Policy.

* https://eclipse.org/projects/dev_process
* https://www.eclipse.org/org/documents/Eclipse_IP_Policy.pdf

## Eclipse Contributor Agreement

In order to be able to contribute to Eclipse Foundation projects you must
electronically sign the Eclipse Contributor Agreement (ECA).

* http://www.eclipse.org/legal/ECA.php

The ECA provides the Eclipse Foundation with a permanent record that you agree
that each of your contributions will comply with the commitments documented in
the Developer Certificate of Origin (DCO). Having an ECA on file associated with
the email address matching the "Author" field of your contribution's Git commits
fulfills the DCO's requirement that you sign-off on your contributions.

For more information, please see the Eclipse Committer Handbook:
https://www.eclipse.org/projects/handbook/#resources-commit

## Contact

Contact the project developers via the project's "dev" list.

* https://accounts.eclipse.org/mailing-list/tractusx-dev


# Contributor Guideline

CX Digital Product Pass

## Content

1. [Why we care](#why-we-care)
2. [Commits](#commits)
3. [Branches](#branches)
4. [Release](#release)
5. [Pull Requests](#pull-requests)
6. [Repositories](#repositories)
</br></br>

## Why we care

We recognize the importance of a commit strategy in ensuring efficient and error-free code management. It facilitates collaborative work, enhances code quality, and helps us deliver high-quality software.
</br></br>

## Commits

All commit messages follow the same syntax and are lowercase: "*{prefix}: {commit comment}*"  
Options for *{prefix}*:  

- **"fix":** correct an issue or error in the codebase
- **"chore":** minor changes and documentation
- **"feat":** **add new functionalities
- **"update":** update existing functionalities
- **"merge":** uses when branches are merged

The *{commit comment}* should briefely describe the relevant content/reason of the commit.  

> Example: "fix: fixed the backend structure" or "feat: added new filter in frontend"  

## Branches

The branching approach is based on the gitflow workflow: [https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)

![Branching Strategy](./docs/media/branchingStrategy.png)

Due to the restriction, that only "Eclipse Committer" can work directly on tractus-x, we extended gitflow with a "forked branch". This enables everybody to fork their own repository and contribute in a structured manner.  

Branch description:

- **"feature":** separate branch for new standalone functionality. This always reflects a JIRA Ticket  
Syntax: "`feature/<JIRA Ticket ID>/<Feature Name>`"
- **"hotfix":** contains fixes for critical issues in production code, produces a release patch from buggy release
version  
Syntax: "`hotfix/<Release version>`"
- **"release":** collects one or more features into releases. To ease the effort on reviewing these releases, they
shall be done countinously, to avoid big and complex Pull Requests. Please also note that these releases
apply only to your forked repository and count not as official product release. These will be created within
tractus-x by the dev-team.  
Syntax: "`release/<Release version>`"

All spaces are substituted by "-"  

Examples for different Branchtypes:

```js
feature/cmp-426/backend-auth
release/v0.6.0
hotfix/v0.6.1
```

## Release

We follow the version enumeration in releases based in the semantic version [https://semver.org/](https://semver.org/):  

Given a version number MAJOR.MINOR.PATCH, increment the:

1. MAJOR version when you make incompatible API changes
2. MINOR version when you add functionality in a backwards compatible manner
3. PATCH version when you make backwards compatible bug fixes

To handle PRs from the forked Repos into tractus-x we bundle small amounts of features into "sub releases". This is
on PATCH level in the semver logic.  

Example of the split of a MINOR release into several "sub releases":
</br></br>

## Pull Requests

Pull Requests are on feature or "sub release" level. The provided Template in GitHub must be filled out.  
The PR naming syntax needs to be in the following structure:

> {Merged branch name as title}: {Major functionality included in the PR}  
>
> Example: Release/v0.5.2: Fixed libraries vulnerabilities

The description of an PR needs to follow this structure:  

> **PR Request**  
> ___
> Why we create this PR?
>  
> << Quick description why the PR is being opened >>  
>
> What we want to achieve with this PR?
>  
> << Quick description of the objective of the PR >>  
>
> What is new?
>
> << Content of the version change log functionalities >>

**Important:** Before releasing / creating Pull Requests into tractus-x: Read and follow the TractusX release guidelines:  
[https://eclipse-tractusx.github.io/docs/release](https://eclipse-tractusx.github.io/docs/release)
</br></br>

## Repositories

There are two types of Repositories:  

- Official Repository: This is tractus-x, which is managed by the Eclipse Foundation. Only elected Eclipse
Comitter with a proven footprint in the open source community can contribute.
- Forked Repositories: Everybody can create their own Fork to further develop the digital product pass. PRs
into tracuts-x will be reviewed by Eclipse Comitters.

## Issue Templates

Issues are used to keep transparent to the Tractus-X community of the bugs we have and what needs to be done to fix them.

### TRG Work Actions Template

> **Introduction**  
> ___
> << Simple explanation why this TRG is done and in what affects the product >>
>
> What needs to be done?
>  
> << List of tasks that need to be done in order to comply with the TRG >> 
>
> - [ ] XXXXX
> - [ ] XXXXXX

### Report a Product Bug Template

> **#Context**  
> ___
> << Describe the context from the problem and what it affects >>  
>  
> **Affects version: vX.X.X**
>
> ### What is the problem?
>  
> << Describe the problem in detail giving the necessary explanation of what is going on when how it was detected >>
> << Add screen shots if possible >>
> 
> #### Example:
>
> << Give an example in how to reproduce the error >>   
> << Add screen shots if possible >>
>
> ### How to solve it?
>
> << If there is a visible/possible solution that can be done, create a simple explanation with examples of how it can be solved >>
> << If the problem is not defined, define what could be possibly the problem >>
