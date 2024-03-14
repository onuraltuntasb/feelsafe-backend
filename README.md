<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/onuraltuntasb/feelsafe-backend">
    <img src="readme-assets/feelsafe-logo.png" alt="Logo" width="280" height="85">
  </a>

<h3 align="center">securing your notes</h3>
  <p align="center">
    <br />
    <a href="https://github.com/onuraltuntasb/feelsafe-backend"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a>
    ·
    <a href="https://github.com/onuraltuntasb/feelsafe-backend/issues">Report Bug</a>
    ·
    <a href="https://github.com/onuraltuntasb/feelsafe-backend/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

![Landing Screen Shot][landing-screenshot]
<br/>
Feelsafe-backend is basicaly a secure store to keep your diaries or notes, whatever you
want. It's using symmetrical encryption to keep your data encrpted in db. This project is created for POC purposes to show how can I develop
Spring boot monolith application with postgres for database management and
maven for build.

The three principles of project:

-   Clean templating for future projects
-   Don't use too abstract tools or patterns unless you have to
-   Follow Spring boot 3-tier architecture with plain jdbcTemplate

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

These are some technologies used to develop the project.

-   [![SpringBoot][SpringBoot]][SpringBoot-url]
-   [![MAVEN][MAVEN]][MAVEN-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

-   JDK 17 or upper
-   Maven

### Installation

1. Clone the repo
    ```sh
    git clone https://github.com/onuraltuntasb/feelsafe-backend
    ```
2. Run feelsafe-backend
    ```sh
    mvn spring-boot:run
    ```
3. Run feelsafe-ui from https://github.com/onuraltuntasb/feelsafe-ui
    ```js
     npm run dev
    ```
4. You can dockerize if you want
   ![Docker Screen Shot][docker-screenshot]

-   create image
    ```js
    docker build -t "your-repo-name"/feelsafe-backend:v1.0 .
    ```
-   create container with docker-compose
    ```js
    docker-compose up
    ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

![Landing Screen Shot][landing-screenshot]
<br/>
Feelsafe uses jwt authentication so you need to sign up first.
![Signin Screen Shot][signin-screenshot]
<br/>
After sign up you can create secure notes or whatever you want.
![Dashboard Screen Shot][dashboard-screenshot]
<br/>
You can list decrypted version of encrypted notes in database.

_For more examples, please refer to the [Documentation](https://github.com/onuraltuntasb/feelsafe-ui)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->

## Roadmap

-   [x] List notes
-   [x] Delete-update notes
-   [ ] Account settings
    -   [ ] Reset Password

See the [open issues](https://github.com/onuraltuntasb/feelsafe-ui/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

Onur Altuntas - onuraltuntasbusiness@gmail.com

Project Link: [https://github.com/onuraltuntasb/feelsafe-backend](https://github.com/onuraltuntasb/feelsafe-ui)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

Some credits for this project

-   [Choose an Open Source License](https://choosealicense.com)
-   [Baeldung AES](https://www.baeldung.com/java-aes-encryption-decryption)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[contributors-shield]: https://img.shields.io/github/contributors/onuraltuntasb/feelsafe-backend.svg?style=for-the-badge
[contributors-url]: https://github.com/onuraltuntasb/feelsafe-backend/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/onuraltuntasb/feelsafe-backend.svg?style=for-the-badge
[forks-url]: https://github.com/onuraltuntasb/feelsafe-backend/network/members
[stars-shield]: https://img.shields.io/github/stars/onuraltuntasb/feelsafe-backend.svg?style=for-the-badge
[stars-url]: https://github.com/onuraltuntasb/feelsafe-backend/stargazers
[issues-shield]: https://img.shields.io/github/issues/onuraltuntasb/feelsafe-backend.svg?style=for-the-badge
[issues-url]: https://github.com/onuraltuntasb/feelsafe-backend/issues
[license-shield]: https://img.shields.io/github/license/onuraltuntasb/feelsafe-backend.svg?style=for-the-badge
[license-url]: https://github.com/onuraltuntasb/feelsafe-backend/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: www.linkedin.com/in/onur-altuntas1
[landing-screenshot]: readme-assets/landing.png
[signin-screenshot]: readme-assets/signin.png
[dashboard-screenshot]: readme-assets/dashboard.png
[docker-screenshot]: readme-assets/docker.png
[SpringBoot]: https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot
[MAVEN]: data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABwAAAAcCAMAAABF0y+mAAAAzFBMVEVHcExwLIZpK4PIIDjJIDfXQS85KGyRHVP1lSKPL27odCbJIjflbSiZI2S5IEeGJnWpIFewIE6rIFWrIFMqJmPQNDPEIDvKJDfLIDb0kSPFIDvxjCPeWSvFIDvJIDe/IEDfWSvfWyvXQC+0IEx+KHyFJnaDJneBJ3m8IETOKTTjainoeSf0lSPndCfLIDbeWCzjaSl+KHuKJXN6KX27IER/KHu2IEmbImWGJnV/KHvKIDazIEy/IEDOJjTaSy7hYCrqeyaJJXPpeCbdVSxkhj7EAAAAOnRSTlMANkom8usWBP0MG8P5Ht7M96m24UdcpDeviP5xPpjl+NzOwUgkqbfgk/Jx5ufdiKnelGOdyo72UWrsTLVmUAAAANZJREFUKJF90VeTgjAUhuGAwQRFql13FcGuW3TXkgSw/P//5Dhe+oVzmWfeM5mEkLfpcV57P33NgHPe09jwsuF8gK3246wvN83a7yJ2nC22Vh6Pi2IFjVnWJL/HNsSvbGLl1hBahdJ6li07ED9E/VOILkPWobQrBK3gUD3DNgxtd9RW/X4Lhol0/5Qa4a2BDA5SLaCxKAqldBsQG34YST+Blp6uoR8c8cux2dzzpjtoxP6fna9TbMTw9vNffBvCbDPVZM+1pt6IYZSgWdUba5aE1VJEv/gAhbgShVrVkEQAAAAASUVORK5CYII=
[MAVEN-url]: https://maven.apache.org/
