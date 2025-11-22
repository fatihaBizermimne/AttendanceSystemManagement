-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 22 nov. 2025 à 16:40
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `attendance_system`
--

-- --------------------------------------------------------

--
-- Structure de la table `attendance`
--

CREATE TABLE `attendance` (
  `aid` int(11) NOT NULL,
  `stime` timestamp NOT NULL DEFAULT current_timestamp(),
  `etime` timestamp NULL DEFAULT NULL,
  `pid` varchar(20) DEFAULT NULL,
  `suid` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `attendance`
--

INSERT INTO `attendance` (`aid`, `stime`, `etime`, `pid`, `suid`, `status`) VALUES
(39, '2025-11-18 15:35:21', NULL, 'S001', 5, 0),
(40, '2025-11-18 15:35:21', NULL, 'S002', 5, 0),
(41, '2025-11-18 15:35:21', NULL, 'S003', 5, 1),
(42, '2025-11-18 15:35:21', NULL, 'S004', 5, 1),
(43, '2025-11-18 15:35:21', NULL, 'S005', 5, 1),
(44, '2025-11-18 15:43:19', NULL, 'S001', 4, 1),
(45, '2025-11-18 15:43:19', NULL, 'S002', 4, 1),
(46, '2025-11-18 15:43:19', NULL, 'S003', 4, 1),
(47, '2025-11-18 15:43:19', NULL, 'S004', 4, 1),
(54, '2025-11-19 18:13:32', NULL, 'S001', 1, 0),
(55, '2025-11-19 18:13:32', NULL, 'S002', 1, 1),
(56, '2025-11-19 18:13:32', NULL, 'S003', 1, 1),
(57, '2025-11-19 18:13:32', NULL, 'S004', 1, 0),
(58, '2025-11-19 18:13:32', NULL, 'S005', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `students`
--

CREATE TABLE `students` (
  `pid` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `students`
--

INSERT INTO `students` (`pid`, `name`, `password`) VALUES
('S001', 'Alice Brown', 'student123'),
('S002', 'Bob Wilson', 'student123'),
('S003', 'Carol Davis', 'student123'),
('S004', 'David Miller', 'student123'),
('S005', 'Eva Garcia', 'student123');

-- --------------------------------------------------------

--
-- Structure de la table `subjects`
--

CREATE TABLE `subjects` (
  `suid` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `subjects`
--

INSERT INTO `subjects` (`suid`, `name`) VALUES
(1, 'Mathematics'),
(2, 'Physics'),
(4, 'Chemistry'),
(5, 'Biology'),
(6, 'Programming 3');

-- --------------------------------------------------------

--
-- Structure de la table `teachers`
--

CREATE TABLE `teachers` (
  `pid` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `teachers`
--

INSERT INTO `teachers` (`pid`, `name`, `password`) VALUES
('T001', 'Dr. Smith', 'password123'),
('T002', 'Prof. Johnson', 'password123');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`aid`),
  ADD KEY `pid` (`pid`),
  ADD KEY `suid` (`suid`);

--
-- Index pour la table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`pid`);

--
-- Index pour la table `subjects`
--
ALTER TABLE `subjects`
  ADD PRIMARY KEY (`suid`);

--
-- Index pour la table `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`pid`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `aid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT pour la table `subjects`
--
ALTER TABLE `subjects`
  MODIFY `suid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `students` (`pid`),
  ADD CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`suid`) REFERENCES `subjects` (`suid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
